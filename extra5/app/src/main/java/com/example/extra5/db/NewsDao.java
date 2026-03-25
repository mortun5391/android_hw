package com.example.extra5.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    LiveData<List<NewsEntity>> all();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<NewsEntity> list);

    @Query("DELETE FROM news")
    void clear();
}
