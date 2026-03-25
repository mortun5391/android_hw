package com.example.extra_3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public boolean done;
    public long createdAt;

    public TaskEntity(String title, boolean done, long createdAt) {
        this.title = title;
        this.done = done;
        this.createdAt = createdAt;
    }
}
