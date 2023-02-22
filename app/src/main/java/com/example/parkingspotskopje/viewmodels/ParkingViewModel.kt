package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Parking

class ParkingViewModel : ViewModel(){
    private val mutableParking= MutableLiveData<Parking>()
    val parking: LiveData<Parking> get()=mutableParking
    fun setParking(p:Parking){
        mutableParking.postValue(p)
    }
}