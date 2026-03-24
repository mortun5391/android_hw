package com.khalilbek.hw8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(Message message);
    
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<Message> getAllMessages();
    
    @Query("DELETE FROM messages")
    void clearMessages();
}
