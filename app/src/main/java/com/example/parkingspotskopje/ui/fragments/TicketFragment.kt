package com.example.parkingspotskopje.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentTicketBinding
import com.example.parkingspotskopje.domain.repository.TicketRepository
import com.example.parkingspotskopje.ui.activities.HomeActivity
import com.example.parkingspotskopje.ui.notifications.NotificationScheduler
import com.google.firebase.auth.FirebaseAuth

class TicketFragment: Fragment(R.layout.fragment_current_ticket) {
    private var _binding: FragmentTicketBinding?=null
    private val binding get()=_binding!!
    private lateinit var auth: FirebaseAuth
    private val ticketRepository: TicketRepository = TicketRepository()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentTicketBinding.bind(view)
        auth= FirebaseAuth.getInstance()
        ticketRepository.getCurrentActiveTicket(auth.currentUser?.email!!){
            binding.tvParkingNameData.text=it!!.parkingName
            binding.tvFromData.text=it!!.fromTime
        }
        binding.btnReleaseSpot.setOnClickListener{
            ticketRepository.getCurrentActiveTicket(auth.currentUser?.email!!){
                binding.tvParkingNameData.text=it!!.parkingName
                binding.tvFromData.text=it!!.fromTime
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure you want to release your spot?")

                builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    ticketRepository.finishTicket(it!!.parkingId,auth.currentUser!!.email!!){
                    }
                    dialog.dismiss()
                    NotificationScheduler.cancelNotifications(requireContext())
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    requireContext().startActivity(intent)
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    // Handle Cancel button click here
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
}