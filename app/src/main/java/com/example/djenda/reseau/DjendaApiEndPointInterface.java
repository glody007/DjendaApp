package com.example.djenda.reseau;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DjendaApiEndPointInterface {

    @GET("/user")
    Call<User> getUser();

    @GET("/produits")
    Call<List<Article>> getArticles();

}
