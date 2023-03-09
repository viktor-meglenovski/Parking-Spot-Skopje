package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Bookmark
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookmarkRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun insertBookmark(userId:String, parkingId:String){
        var bookmark=Bookmark(userId,parkingId)
        database.child("bookmarks").push().setValue(bookmark)
    }

    fun deleteBookmark(userId:String, parkingId:String){
        database.child("bookmarks").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkingIds = mutableListOf<String>()
                for (bookmarkSnapshot in snapshot.children) {
                    val bookmark=bookmarkSnapshot.getValue(Bookmark::class.java)
                    if(bookmark!!.userId==userId && bookmark!!.parkingId==parkingId){
                        bookmarkSnapshot.ref.removeValue()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun findAllBookmarkedParkingsForUser(userId:String,callback: (List<String>) -> Unit) {
        database.child("bookmarks").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkingIds = mutableListOf<String>()
                for (bookmarkSnapshot in snapshot.children) {
                    val bookmark=bookmarkSnapshot.getValue(Bookmark::class.java)
                    if(bookmark!!.userId==userId){
                        parkingIds.add(bookmark.parkingId)
                    }
                }
                callback(parkingIds)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun checkIfBookmarked(userId:String, parkingId:String, callback: (Boolean) -> Unit){
        findAllBookmarkedParkingsForUser(userId){
            callback(it.contains(parkingId))
        }
    }
}