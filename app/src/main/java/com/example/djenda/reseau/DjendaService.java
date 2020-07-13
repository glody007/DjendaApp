package com.example.djenda.reseau;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DjendaService {

    @GET("/user")
    Call<User> getUser();

    @GET("/produits")
    Call<List<Article>> getArticles();

    @POST("/produits/{id}")
    Call<Article> createArticle(@Body Article article);

    @PUT("/produits/{id}")
    Call<Article> updateArticle(@Body Article article);
}
