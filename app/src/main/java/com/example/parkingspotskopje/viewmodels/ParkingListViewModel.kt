package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.repository.ParkingRepository

class ParkingListViewModel(private val parkingRepository: ParkingRepository):ViewModel() {
    private val parkingListLiveData= MutableLiveData<List<Parking>>()
    fun getParkingListLiveData(): LiveData<List<Parking>> = parkingListLiveData
    fun listAllParkings(){
    }
}