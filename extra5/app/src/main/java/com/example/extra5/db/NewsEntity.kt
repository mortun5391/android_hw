package com.example.extra5.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0, val title: String, val description: String, val imageUrl: String)
