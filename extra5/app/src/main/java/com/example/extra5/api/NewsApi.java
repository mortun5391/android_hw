package com.example.extra5.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("v2/top-headlines")
    Call<NewsResponse> top(@Query("country") String country, @Query("apiKey") String key);

    class NewsResponse {
        public List<ArticleDto> articles;
    }

    class ArticleDto {
        public String title;
        public String description;
        public String urlToImage;
    }
}
