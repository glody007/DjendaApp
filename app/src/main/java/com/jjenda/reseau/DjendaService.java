package com.jjenda.reseau;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DjendaService {

    @GET("users/{id}/produits")
    Call<List<Article>> getUserArticles(@Path("id") String id);

    @GET("users/produits")
    Call<List<Article>> getUserArticles();

    @GET("users/posts_restants")
    Call<PostsRestants> getUserPostsRestants();

    @GET("users/{id}")
    Call<User> getUser(@Path("id") String id);

    @GET("produits")
    Call<List<Article>> getArticles();

    @POST("users/{id}/produits")
    Call<List<Article>> createArticle(@Path("id") String id, @Body Article article);

    @POST("produits")
    Call<List<Article>> createArticle(@Body Article article);

    @PUT("users/{id}/produits")
    Call<Article> updateArticle(@Path("id") String id, @Body Article article);

    @GET("auth_endpoint")
    Call<Auth> getAuth();

    @GET("verify_oauth2_token/{token}")
    Call<VerifyOAuth> verify_oauth2_token(@Path("token") String token);

    @POST("send_verification_code")
    Call<Success> sendVerificationCode(@Body Phone phone);

    @POST("register_number")
    Call<Success> registerNumber(@Body Phone phone);

    @POST("check_verification_code")
    Call<Verification> checkVerificationCode(@Body Code code);

    @GET("has_phone_number")
    Call<HasPhoneNumber> hasPhoneNumber();

    @GET("plans")
    Call<List<Plan>> getPlans();

}
