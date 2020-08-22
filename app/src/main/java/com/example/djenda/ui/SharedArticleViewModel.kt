package com.example.djenda.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.djenda.reseau.Article

class SharedArticleViewModel : ViewModel() {

    val selectedArticle = MutableLiveData<Article>()

    fun selectArticle(article : Article) {
        selectedArticle.value = article
    }

}