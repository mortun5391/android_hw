package com.example.extra_1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extra_1.ui.ForecastAdapter;
import com.example.extra_1.ui.WeatherViewModel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView today = findViewById(R.id.todayWeather);
        ForecastAdapter adapter = new ForecastAdapter();
        RecyclerView recyclerView = findViewById(R.id.weekRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        WeatherViewModel vm = new ViewModelProvider(this).get(WeatherViewModel.class);
        vm.getToday().observe(this, today::setText);
        vm.getWeek().observe(this, adapter::submit);

        String apiKey = BuildConfig.OPEN_WEATHER_API_KEY;
        if (apiKey == null || apiKey.isBlank()) {
            today.setText(getString(R.string.error_missing_key));
            return;
        }

        vm.load("Almaty", apiKey);
    }
}
