package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentFavoritesBinding

class FavoritesFragment:Fragment(R.layout.fragment_favorites) {
    private var _binding: FragmentFavoritesBinding?=null
    private val binding get()=_binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentFavoritesBinding.bind(view)
    }
}