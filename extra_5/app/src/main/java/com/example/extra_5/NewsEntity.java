package com.example.extra_5;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String imageUrl;
    public long cachedAt;

    public NewsEntity(String title, String description, String imageUrl, long cachedAt) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cachedAt = cachedAt;
    }
}
