package com.example.parkingspotskopje.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parkingspotskopje.R
import androidx.fragment.app.commit
import com.example.parkingspotskopje.ui.fragments.HomeFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState==null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container_view, HomeFragment())
                setReorderingAllowed(true)
            }
        }
    }
}