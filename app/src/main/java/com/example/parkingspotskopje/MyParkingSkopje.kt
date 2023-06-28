package com.example.parkingspotskopje

import android.app.Application
import com.google.firebase.FirebaseApp

class MyParkingSkopje : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}