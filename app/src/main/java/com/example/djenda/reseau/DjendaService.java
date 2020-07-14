package com.example.djenda.reseau;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DjendaService {

    @GET("/users/{id}/produits")
    Call<List<Article>> getUserArticles(@Path("id") String id);

    @GET("/user")
    Call<User> getUser();

    @GET("/produits")
    Call<List<Article>> getArticles();

    @POST("users/{id}/produits")
    Call<Article> createArticle(@Path("id") String id, @Body Article article);

    @PUT("users/{id}/produits")
    Call<Article> updateArticle(@Path("id") String id, @Body Article article);
}
