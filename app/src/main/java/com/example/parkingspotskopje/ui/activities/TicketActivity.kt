package com.example.parkingspotskopje.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.ui.fragments.TicketFragment

class TicketActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        if (savedInstanceState==null) {
            supportFragmentManager.commit {
                add(R.id.fcv, TicketFragment())
                setReorderingAllowed(true)
            }
        }
    }
}