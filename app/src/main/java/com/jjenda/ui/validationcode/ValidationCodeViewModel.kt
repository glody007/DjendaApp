package com.jjenda.ui.validationcode

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValidationCodeViewModel : ViewModel() {

    var validationCode : String = ""
    val code : Code
        get() = Code(validationCode)

    var repository : Repository = Repository.getInstance()

    private val _eventSendCode = MutableLiveData<Boolean>()
    val eventSendCode : LiveData<Boolean>
        get() = _eventSendCode

    private val _eventErrorWhenCheckingCode = MutableLiveData<Boolean>()
    val eventErrorWhenCheckingCode : LiveData<Boolean>
        get() = _eventErrorWhenCheckingCode

    private val _eventNavigateToArticles = MutableLiveData<Boolean>()
    val eventNavigateToArticles : LiveData<Boolean>
        get() = _eventNavigateToArticles

    private val _eventFalseValidationCode = MutableLiveData<Boolean>()
    val eventFalseValidationCode : LiveData<Boolean>
        get() = _eventFalseValidationCode

    val loading = ObservableField<Boolean>()

    init {
        _eventSendCode.value = false
        _eventErrorWhenCheckingCode.value = false
        _eventNavigateToArticles.value = false
        _eventFalseValidationCode.value = false
        loading.set(false)
    }

    fun onSendMessageFinished() { _eventSendCode.value = false }

    fun onErrorWhenSendingCodeFinished() { _eventErrorWhenCheckingCode.value = false}

    fun onNavigateToArticlesFinished() { _eventNavigateToArticles.value = false }

    fun onFalseValidationCodeFinished() { _eventFalseValidationCode.value = false }

    fun checkVerificationCode() {
        loading.set(true)
        repository.checkVerificationCode(code, object : Callback<Verification> {
            override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
                response.body()?.let {
                    if(it.verify) { _eventNavigateToArticles.value = true }
                    else { _eventFalseValidationCode.value = true }
                }
            }

            override fun onFailure(call: Call<Verification>, t: Throwable) {
                _eventErrorWhenCheckingCode.value = true
                loading.set(false)
            }
        })
    }
}