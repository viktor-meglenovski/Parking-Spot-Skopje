package com.example.parkingspotskopje.domain.model

import java.io.Serializable

data class Review(
    var parkingId:String = "",
    var userId:String = "",
    var userName:String="",
    var stars:Int = 0,
    var comment:String = "",
    var timestamp: String = "")
    : Serializable
{
    var id:String = ""
}