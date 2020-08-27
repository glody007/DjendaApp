package com.example.djenda.ui.articleenvente;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.djenda.reseau.Article;
import com.example.djenda.reseau.Repository;
import com.example.djenda.ui.LoadArticles;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesEnVenteViewModel extends AndroidViewModel implements LoadArticles {

    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Boolean> navigateToArticleDetails;
    private MutableLiveData<Boolean> eventErrorDownloadArticles;
    private MutableLiveData<Boolean> eventLoadArticles;
    public final ObservableField<Boolean> loadingVisible = new ObservableField<>();
    public final ObservableField<Boolean> articlesVisible = new ObservableField<>();
    public final ObservableField<Boolean> errorVisible = new ObservableField<>();
    private Repository repository;

    public ArticlesEnVenteViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        articles = new MutableLiveData<>();
        navigateToArticleDetails = new MutableLiveData<>(false);
        eventErrorDownloadArticles = new MutableLiveData<>(false);
        eventLoadArticles = new MutableLiveData<>(false);
        showLoading();
    }

    public LiveData<List<Article>> getArticles() {
        if(repository.getArticlesCache() != null) { return repository.getArticlesCache(); }
        loadArticles();
        return articles;
    }

    @Override
    public void loadArticles() {
        eventLoadArticles.setValue(true);
        repository.getArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                articles.setValue(response.body());
                repository.setArticlesCache(articles);
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
