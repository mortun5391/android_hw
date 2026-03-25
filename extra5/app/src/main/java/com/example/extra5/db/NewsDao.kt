package com.example.extra5.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news") fun all(): Flow<List<NewsEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(list: List<NewsEntity>)
    @Query("DELETE FROM news") suspend fun clear()
}
