package com.example.parkingspotskopje.domain.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import com.example.parkingspotskopje.domain.viewholders.MessageViewHolder
import com.example.parkingspotskopje.ui.activities.ParkingActivity
import com.google.firebase.auth.FirebaseAuth

class ConversationListAdapter(private val context: Context, private val messagesList:ArrayList<Message> = ArrayList()):
    RecyclerView.Adapter<MessageViewHolder>(){
    var auth=FirebaseAuth.getInstance()
    var parkingRepository:ParkingRepository=ParkingRepository()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.conversation_item,parent,false)
        return MessageViewHolder(view)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        var model=messagesList[position]
        if(auth.currentUser!!.displayName!! == model.senderName){
            holder.userName.text = "You"
            holder.container.setBackgroundColor(Color.parseColor("#ffe0de"))
        }else{
            holder.userName.text = model.senderName
            holder.container.setBackgroundColor(Color.parseColor("#C6FDFF"))
        }
        holder.timestamp.text = model.timestamp
        holder.content.text=model.content
        if(model.parkingId=="NONE"){
            holder.btnGoToParking.visibility= View.GONE
        }else{
            holder.btnGoToParking.visibility= View.VISIBLE
            holder.btnGoToParking.setOnClickListener {
                var intent = Intent(context, ParkingActivity::class.java)
                parkingRepository.getParking(model.parkingId) {
                    intent.putExtra("parking", it)
                    startActivity(context,intent,null)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
    fun updateList(messages: List<Message>) {
        this.messagesList.clear()
        if (messages!=null) {
            this.messagesList.addAll(messages)
        }
        notifyDataSetChanged()
    }
}