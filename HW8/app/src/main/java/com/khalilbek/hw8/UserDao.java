package com.khalilbek.hw8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);
    
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
    
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);
}
