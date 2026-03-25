package com.example.extra1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extra1.ui.ForecastAdapter
import com.example.extra1.ui.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private val vm: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val today = findViewById<TextView>(R.id.todayWeather)
        val adapter = ForecastAdapter()
        findViewById<RecyclerView>(R.id.weekRecycler).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
        }

        vm.today.observe(this) { today.text = it }
        vm.week.observe(this) { adapter.submit(it) }
        vm.load(city = "Almaty", apiKey = "PUT_OPEN_WEATHER_MAP_KEY_HERE")
    }
}
