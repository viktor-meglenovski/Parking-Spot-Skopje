package com.example.parkingspotskopje.domain.model

data class Parking (
    val ID:String,
    val name:String,
    val fee:String,
    val lat:Double,
    val lon:Double,
    val maxCapacity:Int,
    val currentCapacity:Int)
{
    constructor() : this("", "", "", 0.0, 0.0, 0,0)
}
