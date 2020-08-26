package com.example.djenda.ui.mesarticles;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.djenda.reseau.Article;
import com.example.djenda.reseau.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesArticlesViewModel extends AndroidViewModel {

    private Repository repository;
    private MutableLiveData<List<Article>> mesArticles;
    private MutableLiveData<Boolean> navigateToArticleDetails;
    private MutableLiveData<Boolean> eventErrorDownloadArticles;

    public MesArticlesViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        mesArticles = new MutableLiveData<List<Article>>();
        navigateToArticleDetails = new MutableLiveData<Boolean>(false);
        eventErrorDownloadArticles = new MutableLiveData<Boolean>(false);
    }


    public LiveData<List<Article>> getUserArticles() {

        if(repository.getMesArticlesCache() != null) { return repository.getMesArticlesCache(); }

       repository.getUserArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                mesArticles.setValue(response.body());
                repository.setUserArticlesCache(mesArticles);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                eventErrorDownloadArticles.setValue(true);
            }
        });
       return mesArticles;
    }

    public LiveData<Boolean> getNavigateToArticleDetails() {
        return navigateToArticleDetails;
    }

    public LiveData<Boolean> eventErrorDownloadArticles() { return eventErrorDownloadArticles; }

    public void onErrorDownloadArticlesFinished() { eventErrorDownloadArticles.setValue(false); }

    public void onArticleClicked() {
        navigateToArticleDetails.setValue(true);
    }

    public void onArticleDetailsNavigated() {
        navigateToArticleDetails.setValue(false);
    }

}
