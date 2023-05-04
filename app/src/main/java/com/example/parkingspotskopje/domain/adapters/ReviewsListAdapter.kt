package com.example.parkingspotskopje.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Review
import com.example.parkingspotskopje.domain.viewholders.ReviewViewHolder

class ReviewsListAdapter(private val reviewList:ArrayList<Review> = ArrayList<Review>(), private val clicker:OnItemClickListener):
    RecyclerView.Adapter<ReviewViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.review_item,parent,false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        var model=reviewList[position]
        holder.userId.text = model.userName
        holder.stars.text=model.stars.toString()
        holder.comment.text=model.comment
        holder.timestamp.text=model.timestamp
        holder.itemView.setOnClickListener(View.OnClickListener() {
            clicker.onItemClick(model)
        })
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
    fun updateList(reviews: List<Review>) {
        this.reviewList.clear()
        if (reviews!=null) {
            this.reviewList.addAll(reviews)
        }
        notifyDataSetChanged()
    }
}