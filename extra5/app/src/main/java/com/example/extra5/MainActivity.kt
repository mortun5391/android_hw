package com.example.extra5

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.extra5.api.NewsApi
import com.example.extra5.db.NewsDb
import com.example.extra5.db.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = NewsDb.get(this)
        val adapter = NewsAdapter()
        val error = findViewById<TextView>(R.id.errorText)

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(this@MainActivity, android.R.anim.slide_in_left)
        }

        lifecycleScope.launch {
            db.dao().all().collectLatest { cached -> adapter.submit(cached) }
        }

        findViewById<Button>(R.id.reloadBtn).setOnClickListener {
            lifecycleScope.launch {
                try {
                    val api = Retrofit.Builder().baseUrl("https://newsapi.org/")
                        .addConverterFactory(GsonConverterFactory.create()).build().create(NewsApi::class.java)
                    val response = withContext(Dispatchers.IO) { api.top("us", "PUT_NEWS_API_KEY_HERE") }
                    val items = response.articles.map {
                        NewsEntity(title = it.title.orEmpty(), description = it.description.orEmpty(), imageUrl = it.urlToImage.orEmpty())
                    }
                    withContext(Dispatchers.IO) { db.dao().clear(); db.dao().insert(items) }
                    error.text = ""
                } catch (e: Exception) {
                    error.text = "Ошибка сети, показан кэш: ${e.message}"
                }
            }
        }
    }
}
