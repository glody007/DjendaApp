package com.example.myapplication.reseau;

import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("nom")
    String mNom;

    @SerializedName("categorie")
    String mCategorie;

    @SerializedName("description")
    String mDescription;

    @SerializedName("url_photo")
    String mUrlPhoto;

    @SerializedName("prix")
    int mPrix;

    public Article(String nom, String categorie,
                String description, String url_photo, int prix) {
        this.mNom = nom;
        this.mCategorie = categorie;
        this.mDescription = description;
        this.mUrlPhoto = url_photo;
        this.mPrix = prix;
    }

    public String getCategorie() {return this.mCategorie; }

    public String getDescription() {return this.mDescription; }
}