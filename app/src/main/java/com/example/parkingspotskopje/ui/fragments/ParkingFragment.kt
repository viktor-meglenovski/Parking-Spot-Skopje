package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingBinding
import com.example.parkingspotskopje.viewmodels.ParkingViewModel

class ParkingFragment:Fragment(R.layout.fragment_parking) {
    private var _binding:FragmentParkingBinding?=null
    private val binding get()=_binding!!
    private val parkingViewModel: ParkingViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentParkingBinding.bind(view)

        parkingViewModel.parking.observe(viewLifecycleOwner){
            var parking=parkingViewModel.parking.value
            binding.tvName.text=parking?.name
            binding.tvFee.text=parking?.fee
            binding.tvLat.text=parking?.lat.toString()
            binding.tvLon.text=parking?.lon.toString()
            binding.tvMaxCap.text=parking?.maxCapacity.toString()
            binding.tvCurrCap.text=parking?.currentCapacity.toString()
            binding.tvID.text=parking?.ID
        }

    }
}