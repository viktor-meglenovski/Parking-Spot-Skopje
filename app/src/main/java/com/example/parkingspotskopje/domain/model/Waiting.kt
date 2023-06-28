package com.example.parkingspotskopje.domain.model

import java.io.Serializable

data class Waiting(
    var parkingId:String = "",
    var userId:String = "",
    var timestamp: String=""
): Serializable