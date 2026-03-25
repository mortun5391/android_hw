package com.example.extra_1.model;

import java.util.List;

public class WeatherModels {
    public static class ForecastResponse { public List<ForecastItem> list; }
    public static class ForecastItem { public String dt_txt; public MainInfo main; public List<WeatherInfo> weather; }
    public static class MainInfo { public double temp; }
    public static class WeatherInfo { public String description; }

    public static class DayForecast {
        public final String day;
        public final String tempText;
        public DayForecast(String day, String tempText) { this.day = day; this.tempText = tempText; }
    }
}
