package com.example.parkingspotskopje.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.viewholders.MessageViewHolder

class ConversationListAdapter(private val messagesList:ArrayList<Message> = ArrayList()):
    RecyclerView.Adapter<MessageViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.conversation_item,parent,false)
        return MessageViewHolder(view)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        var model=messagesList[position]
        holder.userName.text = model.senderName
        holder.timestamp.text = model.timestamp
        holder.content.text=model.content
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