package com.example.parkingspotskopje.ui.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentFavoritesBinding
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener
import com.example.parkingspotskopje.domain.adapters.ParkingListAdapter
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.ui.activities.ParkingActivity
import com.example.parkingspotskopje.viewmodels.LocationViewModel
import com.example.parkingspotskopje.viewmodels.ParkingListViewModel
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment:Fragment(R.layout.fragment_favorites) {
    private var _binding: FragmentFavoritesBinding?=null
    private val binding get()=_binding!!
    private lateinit var parkingListAdapter: ParkingListAdapter

    private lateinit var parkingListViewModel: ParkingListViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentFavoritesBinding.bind(view)
        parkingListViewModel=ViewModelProvider(this)[ParkingListViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        binding.recyclerViewListParkings.layoutManager= LinearLayoutManager(context)

        val clicker = object: OnItemClickListener {
            override fun onItemClick(obj: Any) {
                var parking=obj as Parking
                val intent : Intent = Intent(context, ParkingActivity::class.java)
                intent.putExtra("parking",parking)
                startActivity(intent)
            }
        }

        parkingListAdapter= ParkingListAdapter(ArrayList<Parking>(),clicker)
        binding.recyclerViewListParkings.adapter=parkingListAdapter

        parkingListViewModel.getFavoriteParkings(auth.currentUser!!.email!!).observe(viewLifecycleOwner) {
            parkingListAdapter.updateParkings(it.sortedBy { x->x.distance })
        }
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        locationViewModel.startLocationUpdates(requireContext()) { permissionGranted ->
            if (permissionGranted) {
                locationViewModel.locationData.observe(viewLifecycleOwner) { location ->
                    // Do something with the location data
                    println(location.latitude.toString()+' '+location.longitude.toString())
                    parkingListViewModel.updateDistanceForAllParkings(location)
                }
            } else {
                // Handle location permission denied
                println("permissions denied")
            }
        }


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, start location updates
                locationViewModel.startLocationUpdates(requireContext()) { permissionGranted ->
                    if (permissionGranted) {
                        locationViewModel.locationData.observe(this) { location ->
                            // Do something with the location data
                            println(location.latitude.toString()+' '+location.longitude.toString())

                        }
                    } else {
                        // Handle location permission denied
                        println("permissions denied")
                    }
                }
            } else {
                // Location permissions not granted, handle this case
                println("permissions not granted")
            }
        }
    }

}