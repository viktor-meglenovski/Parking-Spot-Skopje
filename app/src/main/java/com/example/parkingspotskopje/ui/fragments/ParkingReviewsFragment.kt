package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingReviewsBinding

class ParkingReviewsFragment: Fragment(R.layout.fragment_parking_reviews) {
    private var _binding:FragmentParkingReviewsBinding?=null
    private val binding get()=_binding!!
    //TODO viewmodel for reviews
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentParkingReviewsBinding.bind(view)


    }
}