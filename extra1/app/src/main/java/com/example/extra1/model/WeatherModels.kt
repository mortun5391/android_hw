package com.example.extra1.model

data class ForecastResponse(val list: List<ForecastItem>)
data class ForecastItem(val dt_txt: String, val main: MainInfo, val weather: List<WeatherInfo>)
data class MainInfo(val temp: Double)
data class WeatherInfo(val description: String)

data class DayForecast(val day: String, val tempText: String)
