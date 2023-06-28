package com.example.parkingspotskopje.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingspotskopje.domain.model.Buddy
import com.example.parkingspotskopje.domain.model.Message
import com.example.parkingspotskopje.domain.repository.BuddiesRepository

class ParkingBuddiesViewModel: ViewModel() {
    private val buddiesRepository = BuddiesRepository()
    private val buddiesListLiveData = MutableLiveData<List<Buddy>>()
    private val buddyLiveData = MutableLiveData<Buddy>()
    private val messagesLiveData = MutableLiveData<List<Message>>()

    val buddiesList:LiveData<List<Buddy>> get()=buddiesListLiveData
    val buddy:LiveData<Buddy> get()=buddyLiveData
    val messages:LiveData<List<Message>> get()=messagesLiveData

    fun getAllBuddiesForUser(userId:String):LiveData<List<Buddy>>{
        buddiesRepository.getAllBuddiesForUser(userId){
            buddiesListLiveData.postValue(it)
        }
        return buddiesListLiveData
    }

    fun getBuddy(userId:String, buddyId:String):LiveData<Buddy?>{
        buddiesRepository.getBuddyById(userId,buddyId){
            buddyLiveData.postValue(it)
            messagesLiveData.postValue(it!!.messages)
        }
        return buddyLiveData
    }
    fun setBuddy(buddy:Buddy){
        buddyLiveData.postValue(buddy)
    }
    fun addMessage(senderId:String, receiverId:String, senderName:String, receiverName:String, content:String,parkingId:String){
        buddiesRepository.saveMessageBetweenTwoUsers(senderId,receiverId,senderName,receiverName,content,parkingId)
        getAllMessages(senderId,receiverId)
    }
    fun getAllMessages(senderId: String,receiverId: String):LiveData<List<Message>>{
        buddiesRepository.getBuddyById(senderId,receiverId){
            messagesLiveData.postValue(it!!.messages)
        }
        return messagesLiveData
    }
}