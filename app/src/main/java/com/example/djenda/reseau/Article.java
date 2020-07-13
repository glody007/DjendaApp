package com.example.djenda.reseau;

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

    public String getNom() { return this.mNom; }

    public String getCategorie() { return this.mCategorie; }

    public String getDescription() { return this.mDescription; }

    public String getUrlPhoto() { return this.mUrlPhoto; }

    public int getPrix() { return this.mPrix; }

    public void setNom(String nom) { mNom = nom; }

    public void setCategorie(String categorie) { mCategorie = categorie; }

    public void setDescription(String description) { this.mDescription = description; }

    public void setUrlPhoto(String urlPhoto) { this.mUrlPhoto = urlPhoto; }

    public void setPrix(int prix) { mPrix = prix; }

}