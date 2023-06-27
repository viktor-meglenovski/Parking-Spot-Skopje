package com.example.parkingspotskopje.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/sendReleasedSpotNotification")
    fun sendNotification(@Body requestBody: RequestBody): Response<Unit>
}