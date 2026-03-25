package com.example.extra3.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public long createdAt;

    public TaskEntity(String title, long createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
}
