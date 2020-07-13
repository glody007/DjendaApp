package com.example.djenda.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.djenda.database.article.Article;
import com.example.djenda.database.article.ArticleDao;
import com.example.djenda.database.user.UserDao;

@Database(entities = {Article.class}, version = 1)
public abstract class DjendaDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ArticleDao articleDao();
}
