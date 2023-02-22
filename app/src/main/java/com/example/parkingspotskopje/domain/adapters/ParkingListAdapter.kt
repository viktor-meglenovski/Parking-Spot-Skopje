package com.example.parkingspotskopje.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.viewholders.BasicParkingViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ParkingListAdapter(options: FirebaseRecyclerOptions<Parking>, private val clicker:OnItemClickListener) : FirebaseRecyclerAdapter<Parking, BasicParkingViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicParkingViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.parking_item_basic,parent,false)
        return BasicParkingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasicParkingViewHolder, position: Int, model: Parking) {
        holder.name.text = model.name
        holder.itemView.setOnClickListener(View.OnClickListener() {
            clicker.onItemClick(model)
        })
    }
}