package com.example.extra_5;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsEntity.class}, version = 1, exportSchema = false)
public abstract class NewsDb extends RoomDatabase {
    public abstract NewsDao newsDao();
}
