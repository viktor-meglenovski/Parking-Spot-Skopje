package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.repository.BuddiesRepository

class ParkingBuddiesViewModel: ViewModel() {
    private val buddiesRepository = BuddiesRepository()
    private val buddiesListLiveData = MutableLiveData<List<Buddy>>()

    fun getAllBuddiesForUser(userId:String):LiveData<List<Buddy>>{
        buddiesRepository.getAllBuddiesForUser(userId){
            buddiesListLiveData.postValue(it)
        }
        return buddiesListLiveData
    }
}