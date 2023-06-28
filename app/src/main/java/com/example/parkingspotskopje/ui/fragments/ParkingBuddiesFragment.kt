package com.example.parkingspotskopje.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingBuddiesBinding
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener
import com.example.parkingspotskopje.domain.adapters.ParkingBuddiesListAdapter
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.ui.activities.ConversationActivity
import com.example.parkingspotskopje.viewmodels.ParkingBuddiesViewModel
import com.google.firebase.auth.FirebaseAuth

class ParkingBuddiesFragment:Fragment(R.layout.fragment_parking_buddies) {
    private var _binding:FragmentParkingBuddiesBinding?=null
    private val binding get()=_binding!!
    private lateinit var auth: FirebaseAuth

    private lateinit var parkingBuddiesListAdapter: ParkingBuddiesListAdapter
    private val parkingBuddiesViewModel: ParkingBuddiesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentParkingBuddiesBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        parkingBuddiesViewModel.getAllBuddiesForUser(auth.currentUser!!.email!!)

        binding.recyclerViewListBuddies.layoutManager=LinearLayoutManager(context)
        val cliker=object:OnItemClickListener{
            override fun onItemClick(obj: Any) {
                var buddy = obj as Buddy
                val intent : Intent = Intent(context, ConversationActivity::class.java)
                intent.putExtra("buddy",buddy)
                intent.putExtra("buddyId",buddy.buddyId)
                startActivity(intent)
            }
        }
        parkingBuddiesListAdapter= ParkingBuddiesListAdapter(ArrayList<Buddy>(),cliker)
        binding.recyclerViewListBuddies.adapter=parkingBuddiesListAdapter

        parkingBuddiesViewModel.buddiesList.observe(viewLifecycleOwner){
            parkingBuddiesListAdapter.updateList(it)
            if(parkingBuddiesListAdapter.itemCount==0){
                binding.tvNoBuddiesMessage.visibility=View.VISIBLE
            }else{
                binding.tvNoBuddiesMessage.visibility=View.GONE
            }
        }
    }
}