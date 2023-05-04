package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.model.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

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
    fun getParkingsByName(name:String, callback: (List<Parking>) -> Unit){
        database.child("parkings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parkings = mutableListOf<Parking>()
                for (parkingSnapshot in snapshot.children) {
                    val parking = parkingSnapshot.getValue(Parking::class.java)
                    if(parking?.name!!.toLowerCase().contains(name.toLowerCase())){
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
    fun getReviewsForParking(parkingId:String, callback: (List<Review>) -> Unit){
        database.child("reviews").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = mutableListOf<Review>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    if(review?.parkingId == parkingId){
                        review?.id = reviewSnapshot.key ?: ""
                        reviews.add(review)
                    }
                }
                callback(reviews)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun addReview(parkingId: String,userId:String,userName:String,stars:Int,comment:String,callback: (String) -> Unit){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDateTime = dateFormat.format(currentDate)
        var newReview= Review(parkingId,userId,userName,stars,comment,formattedDateTime)
        database.child("reviews").push().setValue(newReview)
        getCurrentRatingForParking(parkingId) { rating ->
            getCurrentTotalRatingsForParking(parkingId) { totalRatings ->
                val updates = mapOf<String, Any>("rating" to rating, "totalRatings" to totalRatings)
                database.child("parkings").child(parkingId).updateChildren(updates)
                callback(parkingId)
            }
        }
    }

    fun getCurrentRatingForParking(parkingId: String, callback: (Double) -> Unit) {
        var result=0.0
        getReviewsForParking(parkingId) { reviews->
            reviews.forEach{r->
                result+=r.stars
            }
            result/=reviews.size
            callback(result)
        }

    }
    fun getCurrentTotalRatingsForParking(parkingId: String, callback: (Int) -> Unit) {
        getReviewsForParking(parkingId) { reviews ->
            val totalRatings = reviews.size
            callback(totalRatings)
        }
    }
}