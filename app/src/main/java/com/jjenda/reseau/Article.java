package com.jjenda.reseau;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("categorie")
    String mCategorie;

    @SerializedName("description")
    String mDescription;

    @SerializedName("nom")
    String mNom;

    @SerializedName("url_photo")
    String mUrlPhoto;

    @SerializedName("created_at")
    String mCreatedAt;

    @SerializedName("url_thumbnail_photo")
    String mUrlThumbnailPhoto;

    @SerializedName("latitude")
    String mLatitude;

    @SerializedName("longitude")
    String mLongitude;

    @SerializedName("location")
    Double[] mLocation;

    @SerializedName("vendeur_id")
    String mVendeurId;

    @SerializedName("_id")
    Id mId;

    @SerializedName("prix")
    int mPrix;



    public Article(String categorie,
                String description, String urlPhoto,
                String urlThumbnailPhoto, String latitude,
                String longitude, int prix) {
        this.mCategorie = categorie;
        this.mDescription = description;
        this.mUrlPhoto = urlPhoto;
        this.mPrix = prix;
        this.mUrlThumbnailPhoto = urlThumbnailPhoto;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public Article() {
        this.mCategorie = "";
        this.mNom = "none";
        this.mDescription = "";
        this.mUrlPhoto = "";
        this.mPrix = 0;
        this.mUrlThumbnailPhoto = "";
        this.mLatitude = "0";
        this.mLongitude = "0";
    }

    public String getVendeurId() { return this.mVendeurId; }

    public String getId() {return this.mId.getId(); }

    public String getCategorie() { return this.mCategorie; }

    public String getDescription() { return this.mDescription; }

    public String getCreatedAt() { return this.mCreatedAt; }

    public String getUrlPhoto() { return this.mUrlPhoto; }

    public int getPrix() { return this.mPrix; }

    public String getUrlThumbnailPhoto() { return this.mUrlThumbnailPhoto; }

    public Double[] getLocation() { return this.mLocation; }

    public String getLongitude() { return this.mLongitude; }

    public String getLatitude() { return this.mLatitude; }

    public void setCategorie(String categorie) { mCategorie = categorie; }

    public void setDescription(String description) { this.mDescription = description; }

    public void setUrlPhoto(String urlPhoto) { this.mUrlPhoto = urlPhoto; }

    public void setPrix(int prix) { mPrix = prix; }

    public void setUrlThumbnailPhoto(String urlThumbnailPhoto) { this.mUrlThumbnailPhoto = urlThumbnailPhoto; }

    public void setLatitude(String latitude) { this.mLatitude = latitude; }

    public void setLongitude(String longitude) { mLongitude = longitude; }

}