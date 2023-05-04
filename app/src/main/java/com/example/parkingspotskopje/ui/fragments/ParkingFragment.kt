package com.example.parkingspotskopje.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingBinding
import com.example.parkingspotskopje.domain.repository.BookmarkRepository
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import com.google.firebase.auth.FirebaseAuth
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.text.DecimalFormat
import kotlin.math.roundToInt


class ParkingFragment:Fragment(R.layout.fragment_parking) {
    private var _binding:FragmentParkingBinding?=null
    private val binding get()=_binding!!
    private val parkingViewModel: ParkingViewModel by activityViewModels()
    private val bookmarkRepository:BookmarkRepository=BookmarkRepository()
    private lateinit var auth:FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentParkingBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        Configuration.getInstance().load(context, activity?.getPreferences(0))
        parkingViewModel.parking.observe(viewLifecycleOwner){
            var parking=parkingViewModel.parking.value
            binding.tvName.text=parking?.name
            binding.tvFeeData.text=parking?.fee
            binding.tvMaxCapData.text=parking?.maxCapacity.toString()
            binding.tvCurrCapData.text=parking?.currentCapacity.toString()
            binding.tvRegionData.text=parking?.region
            binding.tvRatingData.text=String.format("%.2f", parking?.rating)
            binding.tvNumberOfRatings.text="("+parking?.totalRatings+")"
            if(parking!!.distance<1){
                var metersDouble=parking!!.distance*1000
                var meters=metersDouble.roundToInt()
                binding.tvDistanceData.text=meters.toString() + " meters away"
            }
            else{
                val df = DecimalFormat("#.##")
                binding.tvDistanceData.text=df.format(parking!!.distance)+" km away"
            }

            //map
            binding.map.setTileSource(TileSourceFactory.MAPNIK)
            val mapController = binding.map.controller
            mapController.setZoom(18)
            val startPoint = GeoPoint(parking!!.lat,parking!!.lon);
            mapController.setCenter(startPoint);
            val marker = Marker(binding.map)
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            binding.map.overlays.add(marker)
            binding.btnGoogleMaps.setOnClickListener(){
                val lat=parking?.lat
                val lon=parking?.lon
                val geoUri = "geo:$lat,$lon"
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                mapIntent.setPackage("com.google.android.apps.maps")
                requireContext().startActivity(mapIntent)
            }

            //bookmark
            bookmarkRepository.checkIfBookmarked(auth.currentUser!!.email!!,parking.id){
                if(it){
                    binding.bookmark.setImageResource(R.drawable.heart)

                }else{
                    binding.bookmark.setImageResource(R.drawable.heart_empty)
                }
            }
            binding.bookmark.setOnClickListener(){
                var userId=auth.currentUser!!.email!!
                bookmarkRepository.checkIfBookmarked(auth.currentUser!!.email!!,parking.id){
                    if(it){
                        binding.bookmark.setImageResource(R.drawable.heart_empty)
                        bookmarkRepository.deleteBookmark(userId,parking.id)

                    }else{
                        binding.bookmark.setImageResource(R.drawable.heart)
                        bookmarkRepository.insertBookmark(userId, parking.id)
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.map.onDetach()
    }
}