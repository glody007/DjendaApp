package com.jjenda.reseau;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("nom")
    String mNom;

    @SerializedName("phone_number")
    String mPhoneNumber;

    public User(String nom, String phoneNumber) {
        this.mNom = nom;
        this.mPhoneNumber = phoneNumber;
    }

    public String getNom() {
        return mNom;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
}