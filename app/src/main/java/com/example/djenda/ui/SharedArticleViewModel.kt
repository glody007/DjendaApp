package com.example.djenda.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.djenda.reseau.Article

class SharedArticleViewModel : ViewModel() {

    val selectedArticle = MutableLiveData<Article>()
    private val _eventArticlePosted = MutableLiveData<Boolean>()
    val eventArticlePosted : LiveData<Boolean>
        get() = _eventArticlePosted

    init {
        _eventArticlePosted.value = false
    }

    fun selectArticle(article : Article) {
        selectedArticle.value = article
    }

    fun startEventArticlePosted() {
        _eventArticlePosted.value = true
    }

    fun onArticlePostedFinished() {
        _eventArticlePosted.value = false
    }

}