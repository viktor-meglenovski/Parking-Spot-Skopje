package com.example.parkingspotskopje.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.adapters.ParkingActivityInterfaceAdapter
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import com.google.android.material.tabs.TabLayout

class ParkingActivity : AppCompatActivity() {

    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager: ViewPager

    private val parkingViewModel: ParkingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        parkingViewModel.setParking(intent.getSerializableExtra("parking") as Parking)

        tabLayout=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)

        tabLayout.addTab(tabLayout.newTab().setText("General"))
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"))

        val adapter = ParkingActivityInterfaceAdapter(this,supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter=adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

}