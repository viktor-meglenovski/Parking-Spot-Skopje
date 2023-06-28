package com.example.parkingspotskopje.domain.model

import java.io.Serializable

data class Message(
    var senderId:String = "",
    var senderName:String="",
    var content:String="",
    var timestamp: String = "",
    var parkingId:String = "",
): Serializable
