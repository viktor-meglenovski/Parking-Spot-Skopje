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
import com.example.parkingspotskopje.databinding.FragmentListAllParkingsBinding
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener
import com.example.parkingspotskopje.domain.adapters.ParkingListAdapter
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.ui.activities.ParkingActivity
import com.example.parkingspotskopje.viewmodels.LocationViewModel
import com.example.parkingspotskopje.viewmodels.ParkingListViewModel

class ListAllParkingsFragment : Fragment(R.layout.fragment_list_all_parkings) {
    private var _binding:FragmentListAllParkingsBinding?=null
    private val binding get()=_binding!!

    private lateinit var parkingListAdapter:ParkingListAdapter

    private val parkingListViewModel: ParkingListViewModel by activityViewModels()
    private lateinit var locationViewModel: LocationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentListAllParkingsBinding.bind(view)
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

        parkingListViewModel.getAllParkings().observe(viewLifecycleOwner) {
            parkingListAdapter.updateParkings(it.sortedBy { x->x.distance })
        }
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
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