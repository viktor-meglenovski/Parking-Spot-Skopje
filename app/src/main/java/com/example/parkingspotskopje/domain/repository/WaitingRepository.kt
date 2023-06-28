package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Waiting
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class WaitingRepository{
    private val database = FirebaseDatabase.getInstance().reference

    fun addWaitingItem(userId:String, parkingId:String){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val formattedDateTime = dateFormat.format(currentDate)
        var waitingItem= Waiting(parkingId,userId,formattedDateTime)
        database.child("waitings").push().setValue(waitingItem)
    }

}