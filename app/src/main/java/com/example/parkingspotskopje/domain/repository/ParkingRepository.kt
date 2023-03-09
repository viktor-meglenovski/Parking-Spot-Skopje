package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Parking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ParkingRepository {
    private val database = FirebaseDatabase.getInstance().reference
    private val bookmarkRepository:BookmarkRepository=BookmarkRepository()

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
    fun getParkingsById(ids:List<String>,callback: (List<Parking>) -> Unit){
        database.child("parkings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkings = mutableListOf<Parking>()
                for (parkingSnapshot in snapshot.children) {
                    if(ids.contains(parkingSnapshot.key)){
                        val parking = parkingSnapshot.getValue(Parking::class.java)
                        parking?.id = parkingSnapshot.key ?: ""
                        parkings.add(parking!!)
                    }
                }
                callback(parkings)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
    fun getParkingsByRegion(region:String,callback: (List<Parking>) -> Unit){
        database.child("parkings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkings = mutableListOf<Parking>()
                for (parkingSnapshot in snapshot.children) {
                    val parking = parkingSnapshot.getValue(Parking::class.java)
                    if(region==parking?.region){
                        parking?.id = parkingSnapshot.key ?: ""
                        parkings.add(parking!!)
                    }
                }
                callback(parkings)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
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
    fun getFavoriteParkingsForUser(userId:String, callback: (List<Parking>) -> Unit){
        bookmarkRepository.findAllBookmarkedParkingsForUser(userId){ parkingIds->
            getParkingsById(parkingIds){
                callback(it)
            }
        }
    }
}