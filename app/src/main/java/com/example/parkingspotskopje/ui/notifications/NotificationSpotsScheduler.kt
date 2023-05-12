package com.example.parkingspotskopje.ui.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.parkingspotskopje.domain.model.Parking
import com.example.parkingspotskopje.domain.repository.ParkingRepository
import com.example.parkingspotskopje.ui.activities.ParkingActivity

object NotificationSpotsScheduler {

    private var pendingIntent: PendingIntent? = null
    private const val NOTIFICATION_CHANNEL_ID = "MyNotificationSpotsChannel"
    private const val NOTIFICATION_ID = 1234
    private var parkingObj: Parking? =null
    private var parkingRepository:ParkingRepository = ParkingRepository()

    fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationSpotReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger every 15 seconds
        val intervalMillis = 15_000L
        val triggerAtMillis = System.currentTimeMillis() + intervalMillis

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

    class NotificationSpotReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Create and display the notification
            createNotificationChannel(context)
            showNotification(context)

        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel Name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context) {
        val actionIntent = Intent(context, ParkingActivity::class.java)
        parkingRepository.getParking(parkingObj!!.id){
            parkingObj=it
            actionIntent.putExtra("parking",parkingObj)
            // Create a PendingIntent for the action
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Parking spot status!")
                .setContentText("Tap to check it out!")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent for the tap action
                .setAutoCancel(true) // Close the notification when tapped

            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }

    }
    fun updateParking(parking:Parking){
        parkingObj=parking
    }
}