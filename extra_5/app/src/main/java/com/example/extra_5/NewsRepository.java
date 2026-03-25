package com.example.extra_5;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsRepository {
    private static final String API_KEY = "YOUR_NEWS_API_KEY";
    private final NewsDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public interface Callback { void onError(String msg); }

    public NewsRepository(Context context) {
        NewsDb db = Room.databaseBuilder(context, NewsDb.class, "news.db").build();
        dao = db.newsDao();
    }

    public LiveData<List<NewsEntity>> observeNews() { return dao.observeAll(); }

    public void refreshNews(Callback callback) {
        io.execute(() -> {
            try {
                String endpoint = "https://newsapi.org/v2/top-headlines?country=us&pageSize=20&apiKey=" + API_KEY;
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                StringBuilder json = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) json.append(line);
                }

                JSONObject root = new JSONObject(json.toString());
                JSONArray articles = root.getJSONArray("articles");
                List<NewsEntity> mapped = new ArrayList<>();
                long now = System.currentTimeMillis();
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject a = articles.getJSONObject(i);
                    mapped.add(new NewsEntity(
                            a.optString("title", "Без названия"),
                            a.optString("description", "Нет описания"),
                            a.optString("urlToImage", ""),
                            now - i
                    ));
                }
                dao.clearAll();
                dao.insertAll(mapped);
            } catch (Exception e) {
                callback.onError("Ошибка сети. Показан кэш Room, если он есть.");
            }
        });
    }
}
