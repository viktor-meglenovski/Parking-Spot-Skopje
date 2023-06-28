package com.example.parkingspotskopje.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.ui.fragments.ParkingBuddiesFragment
import com.example.parkingspotskopje.viewmodels.ParkingBuddiesViewModel
import com.example.parkingspotskopje.viewmodels.ParkingViewModel

class ParkingBuddiesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_buddies)
        if (savedInstanceState==null) {
            supportFragmentManager.commit {
                add(R.id.fcv, ParkingBuddiesFragment())
                setReorderingAllowed(true)
            }
        }
    }
}