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

class ConversationActivity : AppCompatActivity() {
    private val buddyViewModel: ParkingBuddyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        val buddy=intent.getSerializableExtra("buddy") as Buddy
        val buddyId=intent.getSerializableExtra("buddyId") as String
        var auth = FirebaseAuth.getInstance()

        buddyViewModel.setBuddy(buddy)
        buddyViewModel.getAllMessages(auth.currentUser!!.email!!,buddyId)

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