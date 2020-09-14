package com.jjenda.ui.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.Plan

class PlansViewModel : ViewModel() {

    val planText = "Vous pouvez poster jusqu'a 30 articles par mois"
    private val _plans = MutableLiveData<List<Plan>>()
    val plans : LiveData<List<Plan>>
        get() = _plans

    init {
        _plans.value = listOf<Plan>(Plan("STANDARD", "can give you many benifice being basic, with 30 photos and more ....")
                                     , Plan("GOLD", "can give you many benifice being basic, with 30 photos and more ...."))
    }

}