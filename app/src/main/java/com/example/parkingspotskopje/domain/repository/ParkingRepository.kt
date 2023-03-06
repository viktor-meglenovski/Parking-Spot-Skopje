package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Parking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ParkingRepository() {
    private val database = FirebaseDatabase.getInstance().reference


    fun getParking(id: String, callback:(Parking?)->Unit) {
        database.child("parkings").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parking = snapshot.getValue(Parking::class.java)
                parking?.id = snapshot.key ?: ""
                callback(parking)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
    fun getAllParkings(callback: (List<Parking>) -> Unit) {
        database.child("parkings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkings = mutableListOf<Parking>()
                for (parkingSnapshot in snapshot.children) {
                    val parking = parkingSnapshot.getValue(Parking::class.java)
                    parking?.id = parkingSnapshot.key ?: ""
                    parkings.add(parking!!)
                }
                callback(parkings)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}