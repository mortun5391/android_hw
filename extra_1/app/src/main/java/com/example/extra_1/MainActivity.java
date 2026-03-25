package com.example.extra_1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView currentTemp;
    private TextView currentDescription;
    private ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTemp = findViewById(R.id.currentTemp);
        currentDescription = findViewById(R.id.currentDescription);
        RecyclerView weeklyRecycler = findViewById(R.id.weeklyRecycler);
        weeklyRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForecastAdapter();
        weeklyRecycler.setAdapter(adapter);

        WeatherViewModel viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel.getTodayWeather().observe(this, this::renderToday);
        viewModel.getWeekWeather().observe(this, this::renderWeek);
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                currentDescription.setText(error);
            }
        });

        viewModel.loadWeather("Moscow");
    }

    private void renderToday(WeatherInfo info) {
        if (info == null) return;
        currentTemp.setText(String.format("%.1f°C", info.getTempCelsius()));
        currentDescription.setText(info.getDescription());
    }

    private void renderWeek(List<WeatherInfo> forecast) {
        if (forecast == null) return;
        adapter.submitList(forecast);
    }
}
