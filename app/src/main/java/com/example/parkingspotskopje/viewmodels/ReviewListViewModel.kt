package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Review
import com.example.parkingspotskopje.domain.repository.ParkingRepository

//class ReviewListViewModel: ViewModel() {
//    private val parkingRepository = ParkingRepository()
//    private val reviewListLiveData = MutableLiveData<List<Review>>()
//    val reviews:LiveData<List<Review>> get()=reviewListLiveData
//    fun getAllReviewsForParking(parkingId: String): LiveData<List<Review>> {
//        parkingRepository.getReviewsForParking(parkingId) { reviews ->
//            reviewListLiveData.postValue(reviews)
//        }
//        return reviewListLiveData
//    }
//    fun addNewReview(parkingId: String, userId:String,userName:String, stars:Int, comment: String){
//        parkingRepository.addReview(parkingId,userId,userName,stars,comment){
//
//        }
//        getAllReviewsForParking(parkingId)
//    }
//}