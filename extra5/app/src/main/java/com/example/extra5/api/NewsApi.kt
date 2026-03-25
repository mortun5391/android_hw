package com.example.extra5.api

import retrofit2.http.GET
import retrofit2.http.Query

data class NewsResponse(val articles: List<ArticleDto>)
data class ArticleDto(val title: String?, val description: String?, val urlToImage: String?)

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun top(@Query("country") country: String, @Query("apiKey") key: String): NewsResponse
}
