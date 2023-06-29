package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.domain.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class BuddiesRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun getAllBuddiesForUser(userId:String, callback:(List<Buddy>)->Unit){
        database.child("buddies").child(userId.replace('.',',')).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val buddies= mutableListOf<Buddy>()
                for(buddySnapshot in snapshot.children){
                    val buddy=buddySnapshot.getValue(Buddy::class.java)
                    buddy?.buddyId=buddySnapshot.key ?:""
                    buddies.add(buddy!!)
                }
                callback(buddies)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
    fun checkIfTwoUsersAreBuddies(userA:String, userB:String, callback: (Boolean) -> Unit){
        getAllBuddiesForUser(userA){
            for(buddy in it){
                if(buddy.buddyId==userB){
                    callback(true)
                }
            }
            callback(false)
        }
    }

    fun getBuddyById(userId: String, buddyId:String, callback: (Buddy?) -> Unit){
        val buddyRef = database.child("buddies").child(userId.replace('.',',')).child(buddyId.replace('.',','))

        buddyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val buddy = dataSnapshot.getValue(Buddy::class.java)
                callback(buddy)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
    fun makeTwoUsersBuddies(userAId:String, userBId:String, userAName:String, userBName:String){
        checkIfTwoUsersAreBuddies(userAId,userBId){
            if(!it){
                var buddyA=Buddy(userAName, emptyList())
                database.child("buddies").child(userBId).push().setValue(buddyA)
                var buddyB=Buddy(userBName, emptyList())
                database.child("buddies").child(userAId).push().setValue(buddyB)
            }
        }
    }

    fun saveMessageBetweenTwoUsers(senderId:String, receiverId:String, senderName:String, receiverName:String, content:String, parkingId:String, callback: (List<Message>) -> Unit){
        //makeTwoUsersBuddies(senderId,receiverId, senderName,receiverName)
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDateTime = dateFormat.format(currentDate)
        var message = Message(senderId, senderName, content,formattedDateTime,parkingId)
        val senderMessagesRef = database.child("buddies").child(senderId).child(receiverId).child("messages")
        val receiverMessagesRef = database.child("buddies").child(receiverId).child(senderId).child("messages")

        senderMessagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                for (childSnapshot in snapshot.children) {
                    val message = childSnapshot.getValue(Message::class.java)
                    message?.let { messages.add(it) }
                }
                messages.add(message)
                senderMessagesRef.setValue(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })

        receiverMessagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                for (childSnapshot in snapshot.children) {
                    val message = childSnapshot.getValue(Message::class.java)
                    message?.let { messages.add(it) }
                }
                messages.add(message)
                receiverMessagesRef.setValue(messages)
                callback(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
        callback(emptyList())
    }
}