package com.jjenda.reseau;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("nom")
    String mNom;

    public User(String nom) {
        this.mNom = nom;
    }
}