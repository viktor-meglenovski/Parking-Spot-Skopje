package com.example.parkingspotskopje.domain.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R

class BuddyViewHolder :RecyclerView.ViewHolder{
    var userId:TextView
    var userName:TextView
    constructor(itemView: View):super(itemView){
        userId=itemView.findViewById(R.id.userId)
        userName=itemView.findViewById(R.id.userName)
    }
}