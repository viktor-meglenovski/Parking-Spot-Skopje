package com.example.parkingspotskopje.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentConversationBinding
import com.example.parkingspotskopje.domain.adapters.ConversationListAdapter
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.repository.BuddiesRepository
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import com.example.parkingspotskopje.ui.activities.ParkingActivity
import com.example.parkingspotskopje.ui.activities.ParkingBuddiesActivity
import com.example.parkingspotskopje.ui.notifications.NotificationSpotsScheduler
import com.example.parkingspotskopje.viewmodels.ParkingBuddiesViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException

class ConversationFragment : Fragment(R.layout.fragment_conversation) {
    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!

    private lateinit var conversationListAdapter: ConversationListAdapter
    private var buddiesRepository: BuddiesRepository = BuddiesRepository()
    private var parkingRepository: ParkingRepository = ParkingRepository()

    private val buddiesViewModel: ParkingBuddiesViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parkingId = arguments?.getString("parkingId")
        _binding = FragmentConversationBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        binding.recyclerViewConversation.layoutManager = layoutManager

        conversationListAdapter = ConversationListAdapter(ArrayList<Message>())

        binding.recyclerViewConversation.adapter = conversationListAdapter

        buddiesViewModel.buddy.observe(viewLifecycleOwner) {
            conversationListAdapter.updateList(it.messages.sortedByDescending { x -> x.timestamp })
            binding.title.text = it.userName
            val messages = buddiesViewModel.messages.value
            val firstMessage = if (messages.isNullOrEmpty()) null else messages[0]
            if(firstMessage!=null && firstMessage.senderId!=auth.currentUser!!.email && !firstMessage.content.contains("Thank")){
                binding.btnSayThanks.visibility=View.VISIBLE
            }else{
                binding.btnSayThanks.visibility=View.GONE
            }
        }

        buddiesViewModel.messages.observe(viewLifecycleOwner) {
            conversationListAdapter.updateList(it.sortedByDescending { x -> x.timestamp })
            val messages = buddiesViewModel.messages.value
            val firstMessage = if (messages.isNullOrEmpty()) null else messages[conversationListAdapter.itemCount-1]
            if(firstMessage!=null && firstMessage.senderId!=auth.currentUser!!.email && firstMessage.parkingId!="NONE"){
                binding.btnSayThanks.visibility=View.VISIBLE
                binding.btnGoToParking.visibility=View.VISIBLE
            }else{
                binding.btnSayThanks.visibility=View.GONE
                binding.btnGoToParking.visibility=View.GONE
            }
            binding.btnGoToParking.setOnClickListener {
                var intent = Intent(context, ParkingActivity::class.java)
                parkingRepository.getParking(firstMessage!!.parkingId) {
                    intent.putExtra("parking", it)
                    startActivity(intent)
                }
            }
        }


        binding.btnSayThanks.setOnClickListener {
            sendThankYouNotification(
                auth.currentUser!!.email!!,
                auth.currentUser!!.displayName!!,
                buddiesViewModel.buddy.value!!.buddyId,
                buddiesViewModel.buddy.value!!.userName
            )
            binding.btnSayThanks.visibility=View.GONE
        }
    }

    private fun sendThankYouNotification(
        senderUserId: String,
        senderUserName: String,
        receiverUserId: String,
        receiverUserName: String
    ) {
        val client = OkHttpClient()

        // Build the URL with query parameters
        val urlBuilder =
            "http://192.168.0.155:8080/api/sayThanksNotification".toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("senderUserId", senderUserId)
        urlBuilder?.addQueryParameter("senderUserName", senderUserName)
        urlBuilder?.addQueryParameter("receiverUserId", receiverUserId)
        urlBuilder?.addQueryParameter("receiverUserName", receiverUserName)
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
        var msg = "Thank you for informing me " + receiverUserName + "!"
        buddiesViewModel.addMessage(
            senderUserId!!.replace('.', ','),
            receiverUserId.replace('.', ','),
            senderUserName!!,
            auth.currentUser!!.displayName!!,
            msg,
            "NONE"
        )
    }
}