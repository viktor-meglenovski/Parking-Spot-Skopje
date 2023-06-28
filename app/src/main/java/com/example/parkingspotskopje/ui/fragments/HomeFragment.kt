package com.example.parkingspotskopje.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentHomeBinding
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import com.example.parkingspotskopje.domain.repository.TicketRepository
import com.example.parkingspotskopje.ui.activities.LoginActivity
import com.example.parkingspotskopje.ui.activities.ParkingBuddiesActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val parkingRepository = ParkingRepository()
    private val ticketRepository: TicketRepository = TicketRepository()

    private lateinit var auth: FirebaseAuth
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.email!!
        val name = auth.currentUser?.displayName

        binding.textView.text=name

        binding.signOutBtn.setOnClickListener{
            auth.signOut()
            startActivity(Intent(context,LoginActivity::class.java))
        }

        binding.listAllParkingsBtn.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.fcv, ListAllParkingsFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.favoritesBtn.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.fcv, FavoritesFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
        binding.searchByNameBtn.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.fcv, SearchByNameFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.searchByRegionBtn.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.fcv, SearchByRegionFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
        binding.btnCurrentTicket.setOnClickListener{
            ticketRepository.getCurrentActiveTicket(email){
                if(it==null){
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("No active ticket!")
                        .setMessage("You do not have a currently active ticket")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    alertDialog.show()
                }else{
                    parentFragmentManager.commit {
                        replace(R.id.fcv, CurrentTicketFragment())
                        setReorderingAllowed(true)
                        addToBackStack(null)
                    }
                }
            }
        }
        binding.parkingBuddiesBtn.setOnClickListener{
            startActivity(Intent(context,ParkingBuddiesActivity::class.java))
        }
    }

}