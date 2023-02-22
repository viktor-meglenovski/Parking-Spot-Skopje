package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentListAllParkingsBinding
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener
import com.example.parkingspotskopje.domain.adapters.ParkingListAdapter
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class ListAllParkingsFragment : Fragment(R.layout.fragment_list_all_parkings) {
    private var _binding:FragmentListAllParkingsBinding?=null
    private val binding get()=_binding!!
    private lateinit var parkingListAdapter:ParkingListAdapter
    private val parkingViewModel: ParkingViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentListAllParkingsBinding.bind(view)

         binding.recyclerViewListParkings.layoutManager= LinearLayoutManager(context)

        var options = FirebaseRecyclerOptions.Builder<Parking>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("parkings"),Parking::class.java)
            .build()

        val clicker = object: OnItemClickListener {
            override fun onItemClick(obj: Any) {
                var parking=obj as Parking
                parkingViewModel.setParking(parking)
                parentFragmentManager.commit {
                    replace(R.id.fragment_container_view, ParkingFragment())
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }

        parkingListAdapter= ParkingListAdapter(options,clicker)
        binding.recyclerViewListParkings.adapter=parkingListAdapter
    }

    override fun onStart() {
        super.onStart()
        parkingListAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        parkingListAdapter.stopListening()
    }
}