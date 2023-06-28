package com.example.parkingspotskopje.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.domain.viewholders.BuddyViewHolder

class ParkingBuddiesListAdapter(private val buddiesList:ArrayList<Buddy> = ArrayList(), private val clicker:OnItemClickListener):
    RecyclerView.Adapter<BuddyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuddyViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.buddy_item,parent,false)
        return BuddyViewHolder(view)
    }
    override fun onBindViewHolder(holder: BuddyViewHolder, position: Int) {
        var model=buddiesList[position]
        holder.userId.text = model.buddyId
        holder.userName.text = model.userName
        holder.itemView.setOnClickListener(View.OnClickListener() {
            clicker.onItemClick(model)
        })
    }

    override fun getItemCount(): Int {
        return buddiesList.size
    }
    fun updateList(buddies: List<Buddy>) {
        this.buddiesList.clear()
        if (buddies!=null) {
            this.buddiesList.addAll(buddies)
        }
        notifyDataSetChanged()
    }
}