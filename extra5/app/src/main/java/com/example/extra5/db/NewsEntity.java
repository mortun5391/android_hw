package com.example.extra5.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String imageUrl;

    public NewsEntity(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
