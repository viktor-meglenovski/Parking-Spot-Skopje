package com.example.parkingspotskopje.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ParkingListViewModel: ViewModel() {
    private val parkingRepository = ParkingRepository()
    private val parkingListLiveData = MutableLiveData<List<Parking>>()

    fun getAllParkings(): LiveData<List<Parking>> {
        parkingRepository.getAllParkings() { parkings ->
            parkingListLiveData.postValue(parkings)
        }
        return parkingListLiveData
    }

    fun updateDistanceForAllParkings(location:Location) {
        var parkings = parkingListLiveData.value ?: ArrayList()
        for (p: Parking in parkings) {
            p.distance = distanceBetweenTwoCoordinates(location.latitude, location.longitude, p.lat, p.lon)
        }
        parkingListLiveData.postValue(parkings)
        sortParkingsByDistance()
    }

    fun distanceBetweenTwoCoordinates(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371 // Radius of the earth in km
        val dLat = DegreesToRadians(lat2 - lat1) // deg2rad below
        val dLon = DegreesToRadians(lon2 - lon1)
        val a: Double = sin(dLat / 2) * sin(dLat / 2) +
                cos(DegreesToRadians(lat1)) * cos(DegreesToRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c: Double = 2 * atan2(sqrt(a), sqrt(1 - a))
        val d: Double = R * c // Distance in km
        return d
    }

    fun DegreesToRadians(deg: Double): Double {
        return deg * (Math.PI / 180)
    }
    fun sortParkingsByDistance(){
        var parkings = parkingListLiveData.value ?: ArrayList()
        parkings.sortedBy { x->x.distance }
        parkingListLiveData.postValue(parkings)
    }
}