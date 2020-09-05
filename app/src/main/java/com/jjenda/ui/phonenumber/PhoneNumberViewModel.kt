package com.jjenda.ui.phonenumber

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.jjenda.reseau.Phone
import com.jjenda.reseau.Repository
import com.jjenda.reseau.Success
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneNumberViewModel : ViewModel() {

    var phoneNumber : String = "+243"
    val phone : Phone
        get() = Phone(phoneNumber)

    var repository : Repository = Repository.getInstance()

    private val _eventSendMessage = MutableLiveData<Boolean>()
    val eventSendMessage : LiveData<Boolean>
        get() = _eventSendMessage

    private val _eventErrorWhenSendingMessage = MutableLiveData<Boolean>()
    val eventErrorWhenSendingMessage : LiveData<Boolean>
        get() = _eventErrorWhenSendingMessage

    private val _eventNavigateToCodeValidation = MutableLiveData<Boolean>()
    val eventNavigateToCodeValidation : LiveData<Boolean>
        get() = _eventNavigateToCodeValidation

    private val _eventCodeNotSended = MutableLiveData<Boolean>()
    val eventCodeNotSended: LiveData<Boolean>
        get() = _eventCodeNotSended

    private val _eventNavigateToArticles = MutableLiveData<Boolean>()
    val eventNavigateToArticles : LiveData<Boolean>
        get() = _eventNavigateToArticles

    private val _eventNavigateToLogin = MutableLiveData<Boolean>()
    val eventNavigateToLogin : LiveData<Boolean>
        get() = _eventNavigateToLogin

    private val _eventRegisterNumber = MutableLiveData<Boolean>()
    val eventRegisterNumber : LiveData<Boolean>
        get() = _eventRegisterNumber

    val loading = ObservableField<Boolean>()

    init {
        _eventSendMessage.value = false
        _eventErrorWhenSendingMessage.value = false
        _eventNavigateToCodeValidation.value = false
        _eventCodeNotSended.value = false
        _eventNavigateToArticles.value = false
        _eventNavigateToLogin.value = false
        _eventRegisterNumber.value = false
        loading.set(false)
    }

    fun onSendMessageFinished() { _eventSendMessage.value = false }

    fun onErrorWhenSendingMessageFinished() { _eventErrorWhenSendingMessage.value = false}

    fun onNavigateToCodeValidationFinished() { _eventNavigateToCodeValidation.value = false }

    fun onCodeNotSendedFinished() { _eventCodeNotSended.value = false }

    fun onNavigateToArticlesFinished() { _eventNavigateToArticles.value = false }

    fun onNavigateToLoginFinished() { _eventNavigateToLogin.value = false }

    fun onRegisterPhoneNumberFinished() { _eventRegisterNumber.value = false }

    fun sendClicked() { _eventRegisterNumber.value = true }

    fun registerNumber() {
        loading.set(true)
        repository.registerNumber(phone, object : Callback<Success> {
            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                response.body()?.let {
                    if(it.success) {
                        Repository.getInstance().storePrefUserId()
                        _eventNavigateToArticles.value = true
                    }
                }

            }

            override fun onFailure(call: Call<Success>, t: Throwable) {
                loading.set(false)
                _eventErrorWhenSendingMessage.value = true
            }
        })
    }

    fun sendVerificationMessage(context : Context) {
        startWaitMessage(context)
        loading.set(true)
        repository.sendVerificationCode(phone, object : Callback<Success> {
                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        response.body()?.let {
                            if(it.success) { _eventNavigateToCodeValidation.value = true }
                            else { _eventCodeNotSended.value = true }
                            Log.d("Success", it.toString())
                        }

                    }

                    override fun onFailure(call: Call<Success>, t: Throwable) {
                        loading.set(false)
                        _eventErrorWhenSendingMessage.value = true
                    }
                })
    }


    fun startWaitMessage(context : Context) {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient( context)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            // ...
        }

        task.addOnFailureListener {
            // Failed to start retriever, inspect Exception for more details
            // ...
        }
    }
}