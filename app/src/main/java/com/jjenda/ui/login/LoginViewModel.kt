package com.jjenda.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jjenda.reseau.HasPhoneNumber
import com.jjenda.reseau.Repository
import com.jjenda.reseau.VerifyOAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    val loading = ObservableField<Boolean>()
    val error = ObservableField<Boolean>()

    private val _eventNavigateToArticlesFragment = MutableLiveData<Boolean>()
    val eventNavigateToArticlesFragment : LiveData<Boolean>
        get() = _eventNavigateToArticlesFragment

    private val _eventNavigateToPhoneNumberFragment = MutableLiveData<Boolean>()
    val eventNavigateToPhoneNumberFragment : LiveData<Boolean>
        get() = _eventNavigateToPhoneNumberFragment

    private val _eventErrorWhenVerifyIfUserHasPhoneNumber = MutableLiveData<Boolean>()
    val eventErrorWhenVerifyIfUserHasPhoneNumber : LiveData<Boolean>
        get() = _eventErrorWhenVerifyIfUserHasPhoneNumber

    private val _eventErrorWhenVerifyOAuthToken = MutableLiveData<Boolean>()
    val eventErrorWhenVerifyOAuthToken  : LiveData<Boolean>
        get() = _eventErrorWhenVerifyOAuthToken

    private val _eventBadIdToken = MutableLiveData<Boolean>()
    val eventBadIdToken  : LiveData<Boolean>
        get() = _eventBadIdToken

    init {
        _eventErrorWhenVerifyIfUserHasPhoneNumber.value = false
        _eventNavigateToArticlesFragment.value = false
        _eventNavigateToPhoneNumberFragment.value = false
        _eventErrorWhenVerifyOAuthToken.value = false
        _eventBadIdToken.value = false
        loading.set(false)
        error.set(false)
    }

    fun onErrorWhenVerifyIfUserHasPhoneNumberFinished() {
        _eventErrorWhenVerifyIfUserHasPhoneNumber.value = false
    }

    fun onBadIdTokenFinished() { _eventBadIdToken.value = false }

    fun onErrorWhenVerifyOAuthTokenFinished() { _eventErrorWhenVerifyOAuthToken.value = false }

    fun onNavigateToArticlesFragmentFinished() { _eventNavigateToArticlesFragment.value = false }

    fun onNavigateToPhoneNumberFragmentFinished() { _eventNavigateToPhoneNumberFragment.value = false }

    private fun storeUserId() {
        Repository.getInstance().storePrefUserId()
    }

    fun userIdStored() : Boolean {
        return !Repository.getInstance().prefUserid.isBlank()
    }

    fun showLoading() { loading.set(true) }

    fun hideLoading() { loading.set(false) }

    fun verifyIfUserHasPhoneNumber() {
        if(userIdStored()) { _eventNavigateToArticlesFragment.value = true }
        else { requestHasPhoneNumber() }
    }

    private fun requestHasPhoneNumber() {
        showLoading()
        Repository.getInstance().hasPhoneNumber(object : Callback<HasPhoneNumber> {

            override fun onResponse(call: Call<HasPhoneNumber>, response: Response<HasPhoneNumber>) {
                response.body()?.let {
                    if(it.hasPhoneNumber) {
                        storeUserId()
                        _eventNavigateToArticlesFragment.value = true
                    }
                    else { _eventNavigateToPhoneNumberFragment.value = true }
                }
            }

            override fun onFailure(call: Call<HasPhoneNumber>, t: Throwable) {
                _eventErrorWhenVerifyIfUserHasPhoneNumber.value = true
                hideLoading()
            }

        })
    }

    fun verify_oauth_token(account: GoogleSignInAccount?) {
        showLoading()
        Repository.getInstance().account = account
        Repository.getInstance().verify_oauth_token(account?.idToken.toString(), object : Callback<VerifyOAuth> {
            override fun onResponse(call: Call<VerifyOAuth>, response: Response<VerifyOAuth>) {
                response.body()?.let {

                    if(it.verify) { requestHasPhoneNumber() }
                    else {
                        _eventBadIdToken.value = true
                        hideLoading()
                    }
                }

            }

            override fun onFailure(call: Call<VerifyOAuth>, t: Throwable) {
                _eventErrorWhenVerifyOAuthToken.value = true
                hideLoading()
            }
        })

    }
}