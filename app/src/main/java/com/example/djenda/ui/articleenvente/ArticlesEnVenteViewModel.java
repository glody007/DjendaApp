package com.example.djenda.ui.articleenvente;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.djenda.reseau.Article;
import com.example.djenda.reseau.Repository;

import java.util.List;

public class ArticlesEnVenteViewModel extends AndroidViewModel {

    private LiveData<List<Article>> articles;
    private Repository repository;

    public ArticlesEnVenteViewModel(@NonNull Application application) {
        super(application);

        init();
    }

    public void init() {
        repository = Repository.getInstance();
        articles = repository.getArticles();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }
}
