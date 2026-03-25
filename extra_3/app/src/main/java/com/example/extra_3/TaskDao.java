package com.example.extra_3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(TaskEntity task);

    @Update
    void update(TaskEntity task);

    @Delete
    void delete(TaskEntity task);

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    LiveData<List<TaskEntity>> allByDateDesc();

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    LiveData<List<TaskEntity>> allByTitleAsc();

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    LiveData<List<TaskEntity>> filterByQuery(String query);
}
