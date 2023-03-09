package com.example.parkingspotskopje.domain.model

import java.io.Serializable

data class Parking(
    var name:String = "",
    var fee:String = "",
    var lat:Double = 0.0,
    var lon:Double = 0.0,
    var maxCapacity:Int = 0,
    var currentCapacity:Int = 0,
    var region:String = ""
) : Serializable
{
    var id:String = ""
    var distance:Double=0.0
}
