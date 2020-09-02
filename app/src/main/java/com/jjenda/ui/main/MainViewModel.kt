package com.jjenda.ui.main


import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    lateinit var articlesPagerAdapter: MainFragment.ArticlesPagerAdapter
    var pagerInited = false

    fun getPagerAdapter(fragment: MainFragment) : MainFragment.ArticlesPagerAdapter {
        if(!pagerInited)  {
            articlesPagerAdapter = MainFragment.ArticlesPagerAdapter(fragment)
            pagerInited = true
        }
        return articlesPagerAdapter
    }
}