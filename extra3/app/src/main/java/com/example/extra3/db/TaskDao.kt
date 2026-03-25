package com.example.extra3.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert suspend fun insert(task: TaskEntity)
    @Update suspend fun update(task: TaskEntity)
    @Delete suspend fun delete(task: TaskEntity)
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC") fun getAllDesc(): Flow<List<TaskEntity>>
    @Query("SELECT * FROM tasks ORDER BY title ASC") fun getAllByTitle(): Flow<List<TaskEntity>>
}
