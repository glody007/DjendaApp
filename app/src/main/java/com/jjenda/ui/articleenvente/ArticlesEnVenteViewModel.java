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
    private MutableLiveData<Boolean> navigateToArticleDetails;
    private MutableLiveData<Boolean> eventErrorDownloadArticles;
    private MutableLiveData<Boolean> eventLoadArticles;
    private MutableLiveData<Boolean> eventLocationLoaded;
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
        eventLocationLoaded = new MutableLiveData<>(false);
    }

    public LiveData<List<Article>> getArticles() {
        if(repository.getArticlesCache() != null) {
            return repository.getArticlesCache();
        }
        loadArticles();
        return articles;
    }

    public Location getLocation() {
        return repository.getLocationCache();
    }

    public void setLocation(Location location) {
        repository.setLocationCache(location);
    }

    @Override
    public void loadArticles() {
        eventLoadArticles.setValue(true);
    }

    protected void loadArticlesFromRepository() {
        repository.getNearArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                articles.setValue(response.body());
                repository.setArticlesCache(articles);
                showArticles();
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

    public LiveData<Boolean> getEventErrorDownloadArticles() { return eventErrorDownloadArticles; }

    public LiveData<Boolean> getEventLoadArticles() { return eventLoadArticles; }

    public LiveData<Boolean> getEventLocationLoaded() { return eventLocationLoaded; }

    public void eventLocationLoaded() { eventLocationLoaded.setValue(true);}

    public void eventErrorDownloadArticles() { eventErrorDownloadArticles.setValue(true);}

    public void onErrorDownloadArticlesFinished() { eventErrorDownloadArticles.setValue(false); }

    public void onEventLocationLoadedFinished() { eventLocationLoaded.setValue(false); }

    public void onLoadArticlesFinished() { eventLoadArticles.setValue(false); }

    public void onArticleClicked() {
        navigateToArticleDetails.setValue(true);
    }

    public void onArticleDetailsNavigated() {
        navigateToArticleDetails.setValue(false);
    }
}
