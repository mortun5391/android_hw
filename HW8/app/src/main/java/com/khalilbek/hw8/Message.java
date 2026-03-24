package com.khalilbek.hw8;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userId;
    public String username;
    public String content;
    public long timestamp;
    
    public Message() {
    }
    
    public Message(int userId, String username, String content, long timestamp) {
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }
}
