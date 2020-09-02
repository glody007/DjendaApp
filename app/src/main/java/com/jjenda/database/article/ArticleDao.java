package com.jjenda.database.article;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM article")
    LiveData<List<Article>> getAll();

    @Insert
    void insert(Article article);

    @Insert
    void insertAll(Article... articles);

    @Delete
    void delete(Article article);

    @Query("DELETE from article")
    void deleteAll();
}
