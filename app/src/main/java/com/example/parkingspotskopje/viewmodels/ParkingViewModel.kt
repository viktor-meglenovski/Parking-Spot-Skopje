package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.repository.ParkingRepository

class ParkingViewModel : ViewModel(){
    private val parkingRepository = ParkingRepository()
    private val parkingLiveData= MutableLiveData<Parking>()
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
}