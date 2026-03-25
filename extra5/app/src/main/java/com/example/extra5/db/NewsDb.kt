package com.example.extra5.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1)
abstract class NewsDb: RoomDatabase() {
    abstract fun dao(): NewsDao
    companion object {
        @Volatile private var I: NewsDb? = null
        fun get(c: Context) = I ?: synchronized(this) { I ?: Room.databaseBuilder(c, NewsDb::class.java, "news.db").build().also { I = it } }
    }
}
