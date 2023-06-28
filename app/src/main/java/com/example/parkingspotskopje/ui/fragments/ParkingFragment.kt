package com.example.parkingspotskopje.ui.fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingBinding
import com.example.parkingspotskopje.domain.repository.BookmarkRepository
import com.example.parkingspotskopje.domain.repository.TicketRepository
import com.example.parkingspotskopje.domain.repository.WaitingRepository
import com.example.parkingspotskopje.ui.notifications.NotificationScheduler
import com.example.parkingspotskopje.ui.notifications.NotificationSpotsScheduler
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.IOException
import java.text.DecimalFormat
import kotlin.math.roundToInt


class ParkingFragment:Fragment(R.layout.fragment_parking) {
    private var _binding:FragmentParkingBinding?=null
    private val binding get()=_binding!!
    private val parkingViewModel: ParkingViewModel by activityViewModels()
    private val bookmarkRepository:BookmarkRepository=BookmarkRepository()
    private val ticketRepository:TicketRepository= TicketRepository()
    private val waitingRepository:WaitingRepository= WaitingRepository()
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
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Bookmark successfully removed!")
                        builder.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
                            dialog.dismiss()
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                    }else{
                        binding.bookmark.setImageResource(R.drawable.heart)
                        bookmarkRepository.insertBookmark(userId, parking.id)
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Bookmark successfully added!")
                        builder.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
                            dialog.dismiss()
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }
            }
            binding.btnGetSpot.setOnClickListener(){
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure you want to book a spot?")

                builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    ticketRepository.addTicket(parking.id,parking.name,auth.currentUser!!.email!!){
                        parkingViewModel.getParking(parking.id)
                    }
                    NotificationScheduler.scheduleNotification(requireContext())
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    // Handle Cancel button click here
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            binding.btnReleaseSpot.setOnClickListener(){
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure you want to release your spot?")

                builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    ticketRepository.finishTicket(parking.id,auth.currentUser!!.email!!){
                        parkingViewModel.getParking(parking.id)
                    }
                    NotificationScheduler.cancelNotifications(requireContext())
                    sendNotificationToUserWaiting(auth.currentUser!!.email!!, auth.currentUser!!.displayName!!, parking.id, parking.name)
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    // Handle Cancel button click here
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }

            binding.btnNotifyMe.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Notification")
                builder.setMessage("You will receive a notification regarding the status of empty spots at this parking in 15 minutes.")

                builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    NotificationSpotsScheduler.updateParking(parking)
                    NotificationSpotsScheduler.scheduleNotification(requireContext())
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    // Handle Cancel button click here
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            binding.btnNotifyMeWhenUserLeaves.setOnClickListener{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Notification")
                builder.setMessage("You will receive a notification when a user leaves a spot.")

                builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    // ADD NEW ITEM IN WAITINGS - handle it with firebase
                    waitingRepository.addWaitingItem(auth.currentUser!!.email!!,parking.id)
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    // Handle Cancel button click here
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            //GET RELEASE NOTIFY
            ticketRepository.getCurrentActiveTicket(auth.currentUser!!.email!!){
                if(it!=null){
                    if(it.parkingId==parking.id){
                        binding.btnReleaseSpot.visibility=View.VISIBLE
                        binding.btnGetSpot.visibility=View.GONE
                        binding.btnNotifyMe.visibility=View.GONE
                        binding.tvNotify.visibility=View.GONE
                        binding.tvAlreadyHasTicket.visibility=View.GONE
                        binding.btnNotifyMeWhenUserLeaves.visibility=View.GONE

                    }else{
                        binding.btnReleaseSpot.visibility=View.GONE
                        binding.btnGetSpot.visibility=View.GONE
                        binding.btnNotifyMe.visibility=View.GONE
                        binding.tvNotify.visibility=View.GONE
                        binding.tvAlreadyHasTicket.visibility=View.VISIBLE
                        binding.btnNotifyMeWhenUserLeaves.visibility=View.GONE
                    }

                }else{
                    if(parking.currentCapacity==0){
                        ticketRepository.checkIfUserIsWaiting(parking.id,auth.currentUser!!.email!!){
                            if(it){
                                binding.btnReleaseSpot.visibility=View.GONE
                                binding.btnGetSpot.visibility=View.GONE
                                binding.btnNotifyMe.visibility=View.GONE
                                binding.tvNotify.visibility=View.VISIBLE
                                binding.tvAlreadyHasTicket.visibility=View.GONE
                                binding.btnNotifyMeWhenUserLeaves.visibility=View.GONE
                            }else{
                                binding.btnReleaseSpot.visibility=View.GONE
                                binding.btnGetSpot.visibility=View.GONE
                                binding.btnNotifyMe.visibility=View.VISIBLE
                                binding.btnNotifyMeWhenUserLeaves.visibility=View.VISIBLE
                                binding.tvNotify.visibility=View.GONE
                                binding.tvAlreadyHasTicket.visibility=View.GONE
                            }
                        }
                    }else{
                        binding.btnReleaseSpot.visibility=View.GONE
                        binding.btnGetSpot.visibility=View.VISIBLE
                        binding.btnNotifyMe.visibility=View.GONE
                        binding.tvNotify.visibility=View.GONE
                        binding.tvAlreadyHasTicket.visibility=View.GONE
                        binding.btnNotifyMeWhenUserLeaves.visibility=View.GONE
                    }

                }
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.map.onDetach()
    }
    private fun sendNotificationToUserWaiting(senderUserId: String, senderUserName:String, parkingId: String, parkingName: String) {
        val client = OkHttpClient()

        // Build the URL with query parameters
        val urlBuilder = "http://192.168.0.155:8080/api/sendReleasedSpotNotification".toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("senderUserId", senderUserId)
        urlBuilder?.addQueryParameter("senderUserName", senderUserName)
        urlBuilder?.addQueryParameter("parkingId", parkingId)
        urlBuilder?.addQueryParameter("parkingName", parkingName)
        val url = urlBuilder?.build()

        // Create the HTTP request
        val request = Request.Builder()
            .url(url!!)
            .get()
            .build()

        // Send the request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {} else {}
            }
        })
    }
}