package com.example.parkingspotskopje.domain.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import org.w3c.dom.Text

class ReviewViewHolder : RecyclerView.ViewHolder{
    var userId: TextView
    var stars: TextView
    var comment: TextView
    var timestamp: TextView
    constructor(itemView: View) : super(itemView) {
        userId = itemView.findViewById(R.id.userId)
        stars=itemView.findViewById(R.id.stars)
        timestamp=itemView.findViewById(R.id.timestamp)
        comment=itemView.findViewById(R.id.comment)
    }
}