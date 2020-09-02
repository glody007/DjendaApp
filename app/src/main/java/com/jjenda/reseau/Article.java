package com.jjenda.reseau;

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

    @SerializedName("url_thumbnail_photo")
    String mUrlThumbnailPhoto;

    @SerializedName("latitude")
    String mLatitude;

    @SerializedName("longitude")
    String mLongitude;

    @SerializedName("prix")
    int mPrix;

    public Article(String nom, String categorie,
                String description, String urlPhoto,
                String urlThumbnailPhoto, String latitude,
                String longitude, int prix) {
        this.mNom = nom;
        this.mCategorie = categorie;
        this.mDescription = description;
        this.mUrlPhoto = urlPhoto;
        this.mPrix = prix;
        this.mUrlThumbnailPhoto = urlThumbnailPhoto;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public Article() {
        this.mNom = "";
        this.mCategorie = "";
        this.mDescription = "";
        this.mUrlPhoto = "";
        this.mPrix = 0;
        this.mUrlThumbnailPhoto = "";
        this.mLatitude = "0";
        this.mLongitude = "0";
    }

    public String getNom() { return this.mNom; }

    public String getCategorie() { return this.mCategorie; }

    public String getDescription() { return this.mDescription; }

    public String getUrlPhoto() { return this.mUrlPhoto; }

    public int getPrix() { return this.mPrix; }

    public String getUrlThumbnailPhoto() { return this.mUrlThumbnailPhoto; }

    public String getLongitude() { return this.mLongitude; }

    public String getLatitude() { return this.mLatitude; }

    public void setNom(String nom) { mNom = nom; }

    public void setCategorie(String categorie) { mCategorie = categorie; }

    public void setDescription(String description) { this.mDescription = description; }

    public void setUrlPhoto(String urlPhoto) { this.mUrlPhoto = urlPhoto; }

    public void setPrix(int prix) { mPrix = prix; }

    public void setUrlThumbnailPhoto(String urlThumbnailPhoto) { this.mUrlThumbnailPhoto = urlThumbnailPhoto; }

    public void setLatitude(String latitude) { this.mLatitude = latitude; }

    public void setLongitude(String longitude) { mLongitude = longitude; }

}