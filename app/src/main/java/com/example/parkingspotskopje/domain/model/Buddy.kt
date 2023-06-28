package com.example.parkingspotskopje.domain.model

import java.io.Serializable

data class Buddy(
    var userName:String = "",
    var messages:List<Message> = emptyList()
) : Serializable
{
    var buddyId:String = ""
}