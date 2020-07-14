package com.example.djenda.ui.mesarticles;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.djenda.reseau.Article;
import com.example.djenda.reseau.Repository;

import java.util.List;

public class MesArticlesViewModel extends AndroidViewModel {

    private LiveData<List<Article>> articles;

    public MesArticlesViewModel(@NonNull Application application) {
        super(application);

        init();
    }

    public void init() {
        Repository repository = Repository.getInstance();
        articles = repository.getUserArticles();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }
}
