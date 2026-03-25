package com.example.extra5.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsEntity.class}, version = 1)
public abstract class NewsDb extends RoomDatabase {
    private static volatile NewsDb I;
    public abstract NewsDao dao();

    public static NewsDb get(Context c) {
        if (I == null) {
            synchronized (NewsDb.class) {
                if (I == null) {
                    I = Room.databaseBuilder(c.getApplicationContext(), NewsDb.class, "news.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return I;
    }
}
