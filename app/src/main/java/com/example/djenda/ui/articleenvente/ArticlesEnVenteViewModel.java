package com.example.djenda.ui.articleenvente;

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

public class ArticlesEnVenteViewModel extends AndroidViewModel {

    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<Boolean> navigateToArticleDetails;
    private Repository repository;

    public ArticlesEnVenteViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance();
        articles = new MutableLiveData<List<Article>>();
        navigateToArticleDetails = new MutableLiveData<Boolean>(false);
    }

    public LiveData<List<Article>> getArticles() {

        if(repository.getArticlesCache() != null) { return repository.getArticlesCache(); }

        repository.getArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                articles.setValue(response.body());
                repository.setArticlesCache(articles);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                // Log error here since request failed
            }
        });
        return articles;
    }

    public LiveData<Boolean> getNavigateToArticleDetails() {
        return navigateToArticleDetails;
    }

    public void onArticleClicked() {
        navigateToArticleDetails.setValue(true);
    }

    public void onArticleDetailsNavigated() {
        navigateToArticleDetails.setValue(false);
    }
}
