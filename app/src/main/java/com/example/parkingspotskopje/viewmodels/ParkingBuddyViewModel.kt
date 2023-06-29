package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.repository.BuddiesRepository

class ParkingBuddyViewModel:ViewModel() {
    private val buddiesRepository=BuddiesRepository()
    private val buddyLiveData = MutableLiveData<Buddy>()
    private val messagesLiveData=MutableLiveData<List<Message>>()
    val messages:LiveData<List<Message>> get()=messagesLiveData
    val buddy:LiveData<Buddy> get()=buddyLiveData

    fun getBuddy(userId:String, buddyId:String): LiveData<Buddy?> {
        buddiesRepository.getBuddyById(userId,buddyId){
            buddyLiveData.postValue(it)
            messagesLiveData.postValue(it!!.messages)
        }
        return buddyLiveData
    }
    fun setBuddy(buddy: Buddy){
        buddyLiveData.postValue(buddy)
    }

    fun getAllMessages(senderId: String,receiverId: String): LiveData<List<Message>> {
        buddiesRepository.getBuddyById(senderId,receiverId){
            messagesLiveData.postValue(it!!.messages)
        }
        return messagesLiveData
    }
    fun addMessage(senderId:String, receiverId:String, senderName:String, receiverName:String, content:String,parkingId:String){
        buddiesRepository.saveMessageBetweenTwoUsers(senderId.replace('.',','),receiverId.replace('.',','),senderName,receiverName,content,parkingId){
            getBuddy(senderId,receiverId)
            messagesLiveData.postValue(it)
        }
    }
}