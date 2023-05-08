package com.example.parkingspotskopje.domain.model

import com.example.parkingspotskopje.domain.enumerations.Status
import java.io.Serializable

data class Ticket(
    var parkingId:String = "",
    var parkingName:String = "",
    var userId:String = "",
    var fromTime:String = "",
    var toTime:String = "",
    var status: Status = Status.STARTED
   ): Serializable
{
    var id:String = ""
}