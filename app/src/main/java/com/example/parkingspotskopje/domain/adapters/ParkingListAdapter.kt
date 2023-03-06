package com.example.parkingspotskopje.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.viewholders.BasicParkingViewHolder
import java.text.DecimalFormat
import kotlin.math.roundToInt


class ParkingListAdapter(private val parkingList:ArrayList<Parking> = ArrayList<Parking>(),private val clicker:OnItemClickListener):Adapter<BasicParkingViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicParkingViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.parking_item_basic,parent,false)
        return BasicParkingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasicParkingViewHolder, position: Int) {
        var model=parkingList[position]
        holder.name.text = model.name
        if(model.distance == 0.0){
            holder.distance.text="Calculating distance"
        }
        else{
            if(model.distance<1){
                var metersDouble=model.distance*1000
                var meters=metersDouble.roundToInt()
                holder.distance.text=meters.toString()+" meters away"

            }
            else{
                val df = DecimalFormat("#.##")
                holder.distance.text=df.format(model.distance)+" km away"
            }
        }
        holder.itemView.setOnClickListener(View.OnClickListener() {
            clicker.onItemClick(model)
        })
    }

    override fun getItemCount(): Int {
        return parkingList.size
    }

    fun updateParkings(parkings:List<Parking>){
        this.parkingList.clear()
        if(parkings!=null){
            this.parkingList.addAll(parkings)
        }
        notifyDataSetChanged()
    }

}