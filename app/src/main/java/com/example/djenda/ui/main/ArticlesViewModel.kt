package com.example.djenda.ui.main


import androidx.lifecycle.ViewModel


class ArticlesViewModel : ViewModel() {

    lateinit var articlesPagerAdapter: ArticlesFragment.ArticlesPagerAdapter
    var pagerInited = false

    fun getPagerAdapter(fragment: ArticlesFragment) : ArticlesFragment.ArticlesPagerAdapter {
        if(!pagerInited)  {
            articlesPagerAdapter = ArticlesFragment.ArticlesPagerAdapter(fragment)
            pagerInited = true
        }
        return articlesPagerAdapter
    }
}