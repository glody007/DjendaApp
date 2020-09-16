package com.jjenda.ui.articleenvente;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jjenda.reseau.Article;
import com.jjenda.reseau.Location;
import com.jjenda.reseau.Repository;
import com.jjenda.ui.LoadArticles;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesEnVenteViewModel extends AndroidViewModel implements LoadArticles {

    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Location> location;
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
        location = new MutableLiveData<>();
        navigateToArticleDetails = new MutableLiveData<>(false);
        eventErrorDownloadArticles = new MutableLiveData<>(false);
        eventLoadArticles = new MutableLiveData<>(false);
        showLoading();
    }

    public LiveData<List<Article>> getArticles() {
        if(repository.getArticlesCache() != null) {
            return repository.getArticlesCache();
        }
        loadArticles();
        return articles;
    }

    public LiveData<Location> getLocation() {
        if(repository.getLocationCache() != null) { return repository.getLocationCache(); }
        return location;
    }

    public void setLocation(Location location) {
        this.location.setValue(location);
        repository.setLocationCache(this.location);
    }

    @Override
    public void loadArticles() {
        eventLoadArticles.setValue(true);
    }

    protected void loadArticlesFromRepository() {
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
