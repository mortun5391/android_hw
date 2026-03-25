package com.example.extra5;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extra5.api.NewsApi;
import com.example.extra5.db.NewsDb;
import com.example.extra5.db.NewsEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsDb db = NewsDb.get(this);
        NewsAdapter adapter = new NewsAdapter();
        TextView error = findViewById(R.id.errorText);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, android.R.anim.slide_in_left));

        db.dao().all().observe(this, adapter::submit);

        ((Button) findViewById(R.id.reloadBtn)).setOnClickListener(v -> {
            NewsApi api = new Retrofit.Builder().baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(NewsApi.class);
            api.top("us", "PUT_NEWS_API_KEY_HERE").enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<NewsApi.NewsResponse> call, Response<NewsApi.NewsResponse> response) {
                    List<NewsEntity> out = new ArrayList<>();
                    if (response.body() != null && response.body().articles != null) {
                        for (NewsApi.ArticleDto dto : response.body().articles) {
                            out.add(new NewsEntity(s(dto.title), s(dto.description), s(dto.urlToImage)));
                        }
                        db.dao().clear();
                        db.dao().insert(out);
                        error.setText("");
                    }
                }

                @Override
                public void onFailure(Call<NewsApi.NewsResponse> call, Throwable t) {
                    error.setText("Ошибка сети, показан кэш: " + t.getMessage());
                }
            });
        });
    }

    private String s(String value) { return value == null ? "" : value; }
}
