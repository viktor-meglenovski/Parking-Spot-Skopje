package com.example.parkingspotskopje.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.ui.fragments.ConversationFragment
import com.example.parkingspotskopje.viewmodels.ParkingBuddyViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException

class ThanksActivity : AppCompatActivity() {
    private val buddyViewModel: ParkingBuddyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        val buddy=intent.getSerializableExtra("buddy") as Buddy
        val buddyId=intent.getSerializableExtra("buddyId") as String
        val senderId=intent.getSerializableExtra("senderId") as String
        val buddyName=intent.getSerializableExtra("buddyName") as String
        val senderName=intent.getSerializableExtra("senderName") as String
        var auth = FirebaseAuth.getInstance()

        buddyViewModel.setBuddy(buddy)
        val msg="Thank you for informing me "+buddyName+"!"
        buddyViewModel.addMessage(senderId,buddyId,senderName,buddyName,msg,"NONE")

        val client = OkHttpClient()

        // Build the URL with query parameters
        val urlBuilder =
            "http://192.168.0.155:8080/api/sayThanksNotification".toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("senderUserId", senderId)
        urlBuilder?.addQueryParameter("senderUserName", senderName)
        urlBuilder?.addQueryParameter("receiverUserId", buddyId)
        urlBuilder?.addQueryParameter("receiverUserName", buddyName)
        val url = urlBuilder?.build()

        // Create the HTTP request
        val request = Request.Builder()
            .url(url!!)
            .get()
            .build()

        // Send the request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                } else {
                }
            }
        })


        if (savedInstanceState==null) {
            if (savedInstanceState==null) {
                supportFragmentManager.commit {
                    add(R.id.fcv, ConversationFragment())
                    setReorderingAllowed(true)
                }
            }
        }
    }
}