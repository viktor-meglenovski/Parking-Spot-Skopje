package com.example.parkingspotskopje.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegionRepository {
    private val database = FirebaseDatabase.getInstance().reference
    fun getAllRegions(callback: (List<String>) -> Unit) {
        database.child("regions").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val regions = mutableListOf<String>()
                regions.add("- Choose a Region -")
                for (regionSnapshot in snapshot.children) {
                    regions.add(regionSnapshot.value.toString())
                }
                callback(regions)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}