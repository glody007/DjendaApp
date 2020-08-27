package com.example.djenda.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    val loading = ObservableField<Boolean>()

    init {
        loading.set(false)
    }
}