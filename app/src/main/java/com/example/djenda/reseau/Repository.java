package com.example.djenda.reseau;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.djenda.application.DjendaApplication;
import com.example.djenda.reseau.interceptor.AddCookiesInterceptor;
import com.example.djenda.reseau.interceptor.ReceivedCookiesInterceptor;

import android.util.Base64;
import java.util.List;


import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Repository {
    private static Repository repository = null;
    DjendaService apiService;
    ImageKitService  imageKitService;
    LiveData<List<Article>> articlesCache = null, mesArticlesCache = null;
    @Inject
    Context appContext;
    byte[] photArticle = null;

    public static Repository getInstance() {
        if(repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    private Repository() {

        DjendaApplication.Companion.getAppComponent().inject(this);

        final String URL_DJENDA = "http://10.0.2.2:5000";
        final String URL_IMAGEKITIO_API = "https://upload.imagekit.io/api/v1/";
        //final String BASE_URL = "https://djendardc.herokuapp.com";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AddCookiesInterceptor(appContext))
                .addInterceptor(new ReceivedCookiesInterceptor(appContext))
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_DJENDA)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.apiService = retrofit.create(DjendaService.class);

        Retrofit imageKit = new Retrofit.Builder()
                .baseUrl(URL_IMAGEKITIO_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.imageKitService = imageKit.create(ImageKitService.class);
    }

    public byte[] getPhotArticle() { return photArticle; }

    public void setPhotArticle(byte[] photo) { photArticle = photo; }

    public void getArticles(Callback<List<Article>> callback) {
        Call<List<Article>> call = this.apiService.getArticles();
        call.enqueue(callback);
    }

    public void getUserArticles(Callback<List<Article>> callback) {
        String id = "1";
        Call<List<Article>> call = this.apiService.getUserArticles(id);
        call.enqueue(callback);
    }

    public LiveData<List<Article>> getArticlesCache() { return articlesCache; }

    public LiveData<List<Article>> getMesArticlesCache() { return  mesArticlesCache; }

    public void setUserArticlesCache(LiveData<List<Article>> articles){ mesArticlesCache = articles; }

    public void setArticlesCache(LiveData<List<Article>> articles){ articlesCache = articles; }

    public void createArticle(Article article, Callback<List<Article>> callback) {
        Call<List<Article>> call = this.apiService.createArticle("1", article);
        call.enqueue(callback);
    }

    public void verify_oauth_token(String id_token, Callback<Verification> callback) {
        Call<Verification> call = this.apiService.verify_oauth2_token(id_token);
        call.enqueue(callback);
    }

    public void get_auth_for_image_upload(Callback<Auth> callback) {
        Call<Auth> call = this.apiService.getAuth();
        call.enqueue(callback);
    }

    public void postPhoto(ImageKitDataToPost data, Callback<ImageKitResponse> callback) {

        if(data != null) {
            String imageString = Base64.encodeToString(photArticle, Base64.DEFAULT);
            Call<ImageKitResponse> call = this.imageKitService.postPhoto(RequestBody.create(MediaType.parse("text/plain"), data.getAuth().getSignature()),
                    RequestBody.create(MediaType.parse("text/plain"), data.getAuth().getExpire()),
                    RequestBody.create(MediaType.parse("text/plain"), data.getAuth().getToken()),
                    RequestBody.create(MediaType.parse("image/*"), imageString),
                    RequestBody.create(MediaType.parse("text/plain"), data.getPublicKey()),
                    RequestBody.create(MediaType.parse("text/plain"), data.getFileName()));

            call.enqueue(callback);
        }
    }

    public void uptdateArticle(Article article, Callback<Article> callBack) {
        Call<Article> call = this.apiService.updateArticle("1",article);
        call.enqueue(callBack);
    }

}
