package com.example.parkingspotskopje.domain.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.parkingspotskopje.ui.fragments.ParkingFragment
import com.example.parkingspotskopje.ui.fragments.ParkingReviewsFragment

internal class ParkingActivityInterfaceAdapter(var context: Context, fm: FragmentManager, val totalTabs:Int):FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                ParkingFragment()
            }

            1-> {
                ParkingReviewsFragment()
            }
            else -> getItem(position)
        }
    }

}