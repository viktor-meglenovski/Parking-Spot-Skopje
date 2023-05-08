package com.example.parkingspotskopje.domain.repository

import com.example.parkingspotskopje.domain.enumerations.Status
import com.example.parkingspotskopje.domain.model.Ticket
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class TicketRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun addTicket(parkingId: String,parkingName:String, userId: String,callback: (Boolean) -> Unit) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val formattedDateTime = dateFormat.format(currentDate)
        val newTicket = Ticket(parkingId,parkingName, userId, formattedDateTime, "", Status.STARTED)
        database.child("tickets").push().setValue(newTicket)

        val currentCapacityRef = database.child("parkings").child(parkingId).child("currentCapacity")
        currentCapacityRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentCapacity = dataSnapshot.getValue(Int::class.java)
                if (currentCapacity != null) {
                    currentCapacityRef.setValue(currentCapacity - 1)
                }
                callback(true)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })

    }

    fun finishTicket(parkingId: String, userId: String,callback: (Boolean) -> Unit) {
        val ticketsRef = database.child("tickets")

        ticketsRef.orderByChild("parkingId").equalTo(parkingId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ticketSnapshot in dataSnapshot.children) {
                    val ticket = ticketSnapshot.getValue(Ticket::class.java)
                    if (ticket != null && ticket.userId == userId && ticket.status == Status.STARTED) {
                        ticket.status = Status.FINISHED
                        val currentDate = Date()
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        val formattedDateTime = dateFormat.format(currentDate)
                        ticket.toTime=formattedDateTime
                        ticketSnapshot.ref.setValue(ticket)

                        val currentCapacityRef = database.child("parkings").child(parkingId).child("currentCapacity")
                        currentCapacityRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val currentCapacity = dataSnapshot.getValue(Int::class.java)
                                if (currentCapacity != null) {
                                    currentCapacityRef.setValue(currentCapacity + 1)
                                    //TODO notify 1 waiting user if there is such
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error if necessary
                            }
                        })
                        break  // Exit the loop after finding and updating the ticket
                    }
                }
                callback(true)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    fun checkIfUserIsWaiting(parkingId: String,userId: String, callback: (Boolean) -> Unit){
        val waitingRef = database.child("waitings")

        waitingRef.orderByChild("parkingId").equalTo(parkingId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isWaiting = false
                for (ticketSnapshot in dataSnapshot.children) {
                    val ticket = ticketSnapshot.getValue(Ticket::class.java)
                    if (ticket != null && ticket.userId == userId) {
                        isWaiting = true
                        break
                    }
                }
                callback(isWaiting)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if necessary
            }
        })
    }
    fun getCurrentActiveTicket(userId:String, callback: (Ticket?) -> Unit){
        val ticketsCollection = database.child("tickets")
        val query = ticketsCollection.orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val activeTickets: MutableList<Ticket> = mutableListOf()

                for (ticketSnapshot in dataSnapshot.children) {
                    val ticket = ticketSnapshot.getValue(Ticket::class.java)

                    if (ticket != null && ticket.status ==Status.STARTED) {
                        activeTickets.add(ticket)
                    }
                }

                val activeTicket = if (activeTickets.isNotEmpty()) activeTickets[0] else null

                callback(activeTicket)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occurred while querying the tickets collection
                // You can log the error or take appropriate action
                callback(null)
            }
        })
    }
}