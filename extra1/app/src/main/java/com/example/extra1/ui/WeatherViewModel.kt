package com.example.extra1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.extra1.api.NetworkSingleton
import com.example.extra1.model.DayForecast
import com.example.extra1.model.ForecastResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherViewModel : ViewModel() {
    private val _today = MutableLiveData<String>()
    val today: LiveData<String> = _today
    private val _week = MutableLiveData<List<DayForecast>>()
    val week: LiveData<List<DayForecast>> = _week

    fun load(city: String, apiKey: String) {
        NetworkSingleton.weatherApi.getForecast(city, apiKey).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                val data = response.body()?.list.orEmpty()
                if (data.isEmpty()) return
                val todayItem = data.first()
                _today.value = "${todayItem.main.temp}°C, ${todayItem.weather.firstOrNull()?.description ?: ""}"

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                _week.value = data.groupBy { LocalDateTime.parse(it.dt_txt, formatter).toLocalDate().toString() }
                    .entries.take(7).map { (d, items) ->
                        DayForecast(d, "${items.map { it.main.temp }.average().toInt()}°C")
                    }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                _today.value = "Ошибка: ${t.message}"
                _week.value = emptyList()
            }
        })
    }
}
