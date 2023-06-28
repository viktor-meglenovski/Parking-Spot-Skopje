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
    }


}