package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.model.Review
import com.example.parkingspotskopje.domain.repository.ParkingRepository

class ParkingViewModel : ViewModel(){
    private val parkingRepository = ParkingRepository()
    private val parkingLiveData= MutableLiveData<Parking>()
    private val reviewListLiveData = MutableLiveData<List<Review>>()
    val reviews:LiveData<List<Review>> get()=reviewListLiveData

    val parking:LiveData<Parking> get()=parkingLiveData
    fun getParking(id:String):LiveData<Parking?>{
        return parkingLiveData.apply {
            parkingRepository.getParking(id){parking->
                value=parking
            }
        }
    }
    fun setParking(parking:Parking){
        parkingLiveData.postValue(parking)
    }


    fun getAllReviewsForParking(parkingId: String): LiveData<List<Review>> {
        parkingRepository.getReviewsForParking(parkingId) { reviews ->
            reviewListLiveData.postValue(reviews)
        }
        return reviewListLiveData
    }
    fun addNewReview(parkingId: String, userId:String,userName:String, stars:Int, comment: String){
        parkingRepository.addReview(parkingId,userId,userName,stars,comment){
            getParking(parkingId)
            getAllReviewsForParking(parkingId)
        }

    }
}