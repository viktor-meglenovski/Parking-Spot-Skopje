package com.example.parkingspotskopje.domain.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener


class BasicParkingViewHolder : RecyclerView.ViewHolder{
    var name:TextView
    var distance:TextView
    constructor(itemView: View) : super(itemView) {
        name = itemView.findViewById(R.id.parkingNameTextView)
        distance=itemView.findViewById(R.id.parkingDistanceTextView)
    }
}