package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentConversationBinding
import com.example.parkingspotskopje.domain.adapters.ConversationListAdapter
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.viewmodels.ParkingBuddyViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException


class ConversationFragment : Fragment(R.layout.fragment_conversation) {
    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!

    private lateinit var conversationListAdapter: ConversationListAdapter

    private val buddyViewModel: ParkingBuddyViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConversationBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        binding.recyclerViewConversation.layoutManager = layoutManager

        conversationListAdapter = ConversationListAdapter(requireContext(),ArrayList<Message>())

        binding.recyclerViewConversation.adapter = conversationListAdapter

        buddyViewModel.buddy.observe(viewLifecycleOwner) {
            binding.title.text = it.userName
        }

        buddyViewModel.messages.observe(viewLifecycleOwner) {
            conversationListAdapter.updateList(it.reversed())
        }
        binding.btnSayThanks.setOnClickListener{
            var msg="Hey "+ buddyViewModel.buddy.value!!.userName+", thank you for informing me!"
            buddyViewModel.addMessage(auth.currentUser!!.email!!, buddyViewModel.buddy.value!!.buddyId,auth.currentUser!!.displayName!!, buddyViewModel.buddy.value!!.userName,msg,"NONE")
            sendThanksNotification(auth.currentUser!!.email!!, buddyViewModel.buddy.value!!.buddyId,auth.currentUser!!.displayName!!, buddyViewModel.buddy.value!!.userName)
            binding.btnSayThanks.visibility=View.GONE
            conversationListAdapter.updateList(buddyViewModel.messages.value!!.reversed())
        }
    }

    fun sendThanksNotification(senderId:String, receiverId:String, senderName:String, receiverName:String){
        val client = OkHttpClient()

        // Build the URL with query parameters
        val urlBuilder =
            "http://192.168.0.155:8080/api/sayThanksNotification".toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("senderUserId", senderId)
        urlBuilder?.addQueryParameter("senderUserName", senderName)
        urlBuilder?.addQueryParameter("receiverUserId", receiverId)
        urlBuilder?.addQueryParameter("receiverUserName", receiverName)
        val url = urlBuilder?.build()

        // Create the HTTP request
        val request = Request.Builder()
            .url(url!!)
            .get()
            .build()

        // Send the request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                } else {
                }
            }
        })
    }


}