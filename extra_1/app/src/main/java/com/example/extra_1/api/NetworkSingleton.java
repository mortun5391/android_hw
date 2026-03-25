package com.example.extra_1.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSingleton {
    private static volatile WeatherApi weatherApi;

    private NetworkSingleton() {}

    public static WeatherApi getWeatherApi() {
        if (weatherApi == null) {
            synchronized (NetworkSingleton.class) {
                if (weatherApi == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.openweathermap.org/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    weatherApi = retrofit.create(WeatherApi.class);
                }
            }
        }
        return weatherApi;
    }
}
