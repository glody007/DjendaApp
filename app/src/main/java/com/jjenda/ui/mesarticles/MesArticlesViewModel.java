package com.jjenda.ui.mesarticles;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jjenda.reseau.Article;
import com.jjenda.reseau.Repository;
import com.jjenda.ui.LoadArticles;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesArticlesViewModel extends AndroidViewModel implements LoadArticles {

    private Repository repository;
    private MutableLiveData<List<Article>> mesArticles;
    private MutableLiveData<Boolean> navigateToArticleDetails;
    private MutableLiveData<Boolean> eventErrorDownloadArticles;
    private MutableLiveData<Boolean> eventLoadArticles;
    public final ObservableField<Boolean> loadingVisible = new ObservableField<>();
    public final ObservableField<Boolean> articlesVisible = new ObservableField<>();
    public final ObservableField<Boolean> errorVisible = new ObservableField<>();

    public MesArticlesViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        mesArticles = new MutableLiveData<>();
        navigateToArticleDetails = new MutableLiveData<>(false);
        eventErrorDownloadArticles = new MutableLiveData<>(false);
        eventLoadArticles = new MutableLiveData<>(false);
    }


    public LiveData<List<Article>> getUserArticles() {
        if(repository.getMesArticlesCache() != null) { return repository.getMesArticlesCache(); }
       loadArticles();
       return mesArticles;
    }

    @Override
    public void loadArticles() {
        eventLoadArticles.setValue(true);
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
    }

    public void showLoading() {
        loadingVisible.set(true);
        articlesVisible.set(false);
        errorVisible.set(false);
    }

    public void showErrorDownload() {
        loadingVisible.set(false);
        articlesVisible.set(false);
        errorVisible.set(true);
    }

    public void showArticles() {
        loadingVisible.set(false);
        articlesVisible.set(true);
        errorVisible.set(false);
    }

    public LiveData<Boolean> getNavigateToArticleDetails() {
        return navigateToArticleDetails;
    }

    public LiveData<Boolean> eventErrorDownloadArticles() { return eventErrorDownloadArticles; }

    public LiveData<Boolean> eventLoadArticles() { return eventLoadArticles; }

    public void onErrorDownloadArticlesFinished() { eventErrorDownloadArticles.setValue(false); }

    public void onLoadArticlesFinished() { eventLoadArticles.setValue(false); }

    public void onArticleClicked() {
        navigateToArticleDetails.setValue(true);
    }

    public void onArticleDetailsNavigated() {
        navigateToArticleDetails.setValue(false);
    }

}
