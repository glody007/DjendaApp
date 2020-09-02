package com.jjenda.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jjenda.database.article.Article;
import com.jjenda.database.article.ArticleDao;
import com.jjenda.database.user.UserDao;

@Database(entities = {Article.class}, version = 1)
public abstract class DjendaDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ArticleDao articleDao();
}
