package com.jjenda.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.Article
import com.jjenda.reseau.Location

class SharedArticleViewModel : ViewModel() {

    val selectedArticle = MutableLiveData<Article>()
    val myLocation = MutableLiveData<Location>()
    private val _eventArticlePosted = MutableLiveData<Boolean>()
    val eventArticlePosted : LiveData<Boolean>
        get() = _eventArticlePosted

    init {
        _eventArticlePosted.value = false
    }

    fun selectArticle(article : Article) {
        selectedArticle.value = article
    }

    fun setLocation(location: Location) {
        myLocation.value = location
    }

    fun startEventArticlePosted() {
        _eventArticlePosted.value = true
    }

    fun onArticlePostedFinished() {
        _eventArticlePosted.value = false
    }

}