package com.example.parkingspotskopje.api

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.parkingspotskopje.domain.repository.BuddiesRepository
import com.example.parkingspotskopje.ui.activities.ConversationActivity
import com.example.parkingspotskopje.ui.activities.ThanksActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var auth: FirebaseAuth=FirebaseAuth.getInstance()
    private var buddiesRepository:BuddiesRepository = BuddiesRepository()



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            val data = remoteMessage.data
            if(data["type"]=="THANKS"){
                val senderId = data["senderId"]
               showNotificationThanks(title,body,senderId)
            }
            else{
                val senderId = data["senderId"]
                val senderName = data["senderName"]
                val parkingName = data["parkingName"]
                val parkingId=data["parkingId"]
                var msg="Hey "+ auth.currentUser!!.displayName!!+" I have released a spot at "+parkingName+"!"
                buddiesRepository.saveMessageBetweenTwoUsers(auth.currentUser!!.email!!.replace('.',','),senderId!!.replace('.',','),senderName!!, auth.currentUser!!.displayName!!, msg,parkingId!!){}
                showNotification(title, body, senderId)
            }
        }
    }

    private fun showNotificationThanks(title: String?, body: String?, senderId: String?) {
        //when notification is pressed, open the chat in parking buddies activity
        val actionIntent= Intent(applicationContext,ConversationActivity::class.java)
        buddiesRepository.getBuddyById(auth.currentUser!!.email!!,senderId!!.replace('.',',')){
            actionIntent.putExtra("buddy",it)
            actionIntent.putExtra("buddyId",senderId)
            val pendingIntent=PendingIntent.getActivity(
                applicationContext,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent for the tap action
                .setAutoCancel(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Display the notification
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    private fun showNotification(title: String?, body: String?, senderId: String?) {
        //when notification is pressed, open the chat in parking buddies activity
        val actionIntent= Intent(applicationContext, ThanksActivity::class.java)
        buddiesRepository.getBuddyById(auth.currentUser!!.email!!,senderId!!.replace('.',',')){
            actionIntent.putExtra("buddy",it)
            actionIntent.putExtra("buddyName",it!!.userName)
            actionIntent.putExtra("buddyId",senderId)
            actionIntent.putExtra("senderId",auth.currentUser!!.email!!)
            actionIntent.putExtra("senderName",auth.currentUser!!.displayName!!)
            val pendingIntent=PendingIntent.getActivity(
                applicationContext,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent for the tap action
                .setAutoCancel(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Display the notification
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}