package com.example.djenda.ui.articledetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleDetailsViewModel : ViewModel() {
    private val _eventSendMessage = MutableLiveData<Boolean>()
    val eventSendMessage : LiveData<Boolean>
        get() = _eventSendMessage

    private val _eventCall = MutableLiveData<Boolean>()
    val eventCall : LiveData<Boolean>
        get() = _eventCall

    init {
        _eventCall.value = false
        _eventSendMessage.value = false
    }

    fun onSendMessage() { _eventSendMessage.value = true }

    fun onCall() { _eventCall.value = true }

    fun onSendMessageFinished() { _eventSendMessage.value = false }

    fun onCallFinished() { _eventCall.value = false }

}