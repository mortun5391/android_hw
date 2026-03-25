package com.example.extra1.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkSingleton {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi: WeatherApi by lazy { retrofit.create(WeatherApi::class.java) }
}
