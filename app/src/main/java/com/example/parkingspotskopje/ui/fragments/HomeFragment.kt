package com.example.parkingspotskopje.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentHomeBinding
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import com.example.parkingspotskopje.ui.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val parkingRepository = ParkingRepository()

    private lateinit var auth: FirebaseAuth
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.email
        val name = auth.currentUser?.displayName

        binding.textView.text="Welcome back "+email

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
    }
}