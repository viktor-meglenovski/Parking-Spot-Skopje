package com.example.parkingspotskopje.domain.viewholders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R

class MessageViewHolder : RecyclerView.ViewHolder{
    var userName: TextView
    var timestamp: TextView
    var content: TextView
    var btnGoToParking: Button
    var container: ConstraintLayout
    constructor(itemView: View):super(itemView){
        userName=itemView.findViewById(R.id.userName)
        timestamp=itemView.findViewById(R.id.timestamp)
        content=itemView.findViewById(R.id.content)
        btnGoToParking=itemView.findViewById(R.id.btnGoToParking)
        container=itemView.findViewById(R.id.container)
    }
}