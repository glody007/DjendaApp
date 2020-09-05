package com.jjenda.ui.articledetails

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.Repository
import com.jjenda.reseau.User
import com.jjenda.ui.LoadArticles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleDetailsViewModel : ViewModel(), LoadArticles {
    var number = ""
    var vendeurId = ""
    val loadingVisible = ObservableField<Boolean>()
    val errorVisible = ObservableField<Boolean>()
    val infoVisible = ObservableField<Boolean>()

    private val _eventSendMessage = MutableLiveData<Boolean>()
    val eventSendMessage : LiveData<Boolean>
        get() = _eventSendMessage

    private val _eventCall = MutableLiveData<Boolean>()
    val eventCall : LiveData<Boolean>
        get() = _eventCall

    private val _eventErrorWhenGetUserInfo = MutableLiveData<Boolean>()
    val eventErrorWhenGetUserInfo : LiveData<Boolean>
        get() = _eventErrorWhenGetUserInfo

    init {
        _eventCall.value = false
        _eventSendMessage.value = false
        _eventErrorWhenGetUserInfo.value = false
    }


    fun getUserInfo() {
        showLoading()
        Repository.getInstance().getUser(vendeurId, object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body()?.apply {
                    number = this.phoneNumber
                    showInfo()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _eventErrorWhenGetUserInfo.value = true
                showErrorDownload()
            }

        })
    }

    fun showLoading() {
        loadingVisible.set(true)
        infoVisible.set(false)
        errorVisible.set(false)
    }

    fun showErrorDownload() {
        loadingVisible.set(false)
        infoVisible.set(false)
        errorVisible.set(true)
    }

    fun showInfo()
    {
        loadingVisible.set(false)
        infoVisible.set(true)
        errorVisible.set(false)
    }

    fun onSendMessage() { _eventSendMessage.value = true }

    fun onCall() { _eventCall.value = true }

    fun onSendMessageFinished() { _eventSendMessage.value = false }

    fun onCallFinished() { _eventCall.value = false }

    fun onGetUserInfoFinished() { _eventErrorWhenGetUserInfo.value = false }

    override fun loadArticles() { getUserInfo() }

}