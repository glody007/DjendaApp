package com.example.djenda.reseau;

import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    DjendaApiEndPointInterface apiService;

    public NetworkUtils() {
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

        this.apiService = retrofit.create(DjendaApiEndPointInterface.class);

    }

    public void getArticles(Callback<List<Article>> callBack) {
        Call<List<Article>> call = this.apiService.getArticles();
        call.enqueue(callBack);
    }








}
