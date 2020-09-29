package com.jjenda.ui.plans

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.Plan
import com.jjenda.reseau.Repository
import com.jjenda.reseau.User
import com.jjenda.ui.LoadArticles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlansViewModel : ViewModel(), LoadArticles {

    val planText = "Vous pouvez poster jusqu'a 30 articles par mois"
    private val _plans = MutableLiveData<List<Plan>>()
    val plans : LiveData<List<Plan>>
        get() = _plans

    val loadingVisible = ObservableField<Boolean>()
    val errorVisible = ObservableField<Boolean>()
    val plansVisible = ObservableField<Boolean>()

    var forfait = ""

    fun getPlans() {
        showLoading()
        Repository.getInstance().getPlans(object : Callback<List<Plan>> {
            override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                response.body()?.apply {
                    _plans.value = this
                    showPlans()
                }
            }

            override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                showErrorDownload()
            }

        })
    }

    fun showLoading() {
        loadingVisible.set(true)
        plansVisible.set(false)
        errorVisible.set(false)
    }

    fun showErrorDownload() {
        loadingVisible.set(false)
        plansVisible.set(false)
        errorVisible.set(true)
    }

    fun showPlans()
    {
        loadingVisible.set(false)
        plansVisible.set(true)
        errorVisible.set(false)
    }

    override fun loadArticles() {
        Log.d("Load", "load")
        getPlans()
    }

}