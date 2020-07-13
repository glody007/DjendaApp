package com.example.djenda.reseau;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static Repository repository = null;
    DjendaService apiService;
    LiveData<List<Article>> articlesCache = null;

    public static Repository getInstance() {
        if(repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    private Repository() {
        final String BASE_URL = "http://10.0.2.2:5000";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.apiService = retrofit.create(DjendaService.class);

    }

    public LiveData<List<Article>> getArticles() {
        if(articlesCache != null) {
            return articlesCache;
        }

        Call<List<Article>> call = this.apiService.getArticles();
        final MutableLiveData<List<Article>> articles = new MutableLiveData<List<Article>>();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                int statusCode = response.code();
                articles.setValue(response.body());
                articlesCache = articles;
            }

            @Override
            public void onFailure(Call<List<Article>>call, Throwable t) {
                // Log error here since request failed
            }
        });
        return articles;
    }

    public void createArticle(Article article, Callback<Article> callBack) {
        Call<Article> call = this.apiService.createArticle(article);
        call.enqueue(callBack);
    }

    public void uptdateArticle(Article article, Callback<Article> callBack) {
        Call<Article> call = this.apiService.updateArticle(article);
        call.enqueue(callBack);
    }

}
