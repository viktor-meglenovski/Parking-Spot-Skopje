package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingBinding
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

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
            binding.tvFeeData.text=parking?.fee
            binding.tvLatData.text=parking?.lat.toString()
            binding.tvLonData.text=parking?.lon.toString()
            binding.tvMaxCapData.text=parking?.maxCapacity.toString()
            binding.tvCurrCapData.text=parking?.currentCapacity.toString()
            if(parking!!.distance<1){
                var metersDouble=parking!!.distance*1000
                var meters=metersDouble.roundToInt()
                binding.tvDistanceData.text=meters.toString() + " meters away"
            }
            else{
                val df = DecimalFormat("#.##")
                binding.tvDistanceData.text=df.format(parking!!.distance)+" km away"
            }
        }
    }
}