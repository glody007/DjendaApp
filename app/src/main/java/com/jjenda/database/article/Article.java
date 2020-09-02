package com.jjenda.database.article;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Article {
    public static final int POUR_PLUS_TARD = 1,
                            POUR_MOI = 2;

    @PrimaryKey
    public int aid;

    @ColumnInfo(name = "nom")
    public String nom;

    @ColumnInfo(name = "categorie")
    public String categorie;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "url_photo")
    public String urlPhoto;

    @ColumnInfo(name = "prix")
    public int prix;

    @ColumnInfo(name = "type")
    public int type;
}
