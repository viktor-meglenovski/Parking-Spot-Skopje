package com.example.parkingspotskopje.domain

import com.example.parkingspotskopje.domain.model.Parking

interface RemoteDataSource {
    suspend fun listAllParkings():List<Parking>
}