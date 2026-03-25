package com.example.extra_1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherRepository {
    private static volatile WeatherRepository instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String API_KEY = "YOUR_OPENWEATHERMAP_API_KEY";

    public interface Callback {
        void onSuccess(WeatherInfo today, List<WeatherInfo> week);
        void onError(String message);
    }

    private WeatherRepository() {}

    public static WeatherRepository getInstance() {
        if (instance == null) {
            synchronized (WeatherRepository.class) {
                if (instance == null) {
                    instance = new WeatherRepository();
                }
            }
        }
        return instance;
    }

    public void loadWeather(String city, Callback callback) {
        executor.execute(() -> {
            try {
                String endpoint = "https://api.openweathermap.org/data/2.5/forecast?q=" + city
                        + "&appid=" + API_KEY + "&units=metric&lang=ru";
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                StringBuilder json = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) json.append(line);
                }

                JSONObject root = new JSONObject(json.toString());
                JSONArray list = root.getJSONArray("list");
                List<WeatherInfo> perDay = new ArrayList<>();
                Map<String, WeatherInfo> grouped = new HashMap<>();

                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    String date = item.getString("dt_txt").split(" ")[0];
                    double temp = item.getJSONObject("main").getDouble("temp");
                    String description = item.getJSONArray("weather").getJSONObject(0).getString("description");
                    if (!grouped.containsKey(date)) {
                        grouped.put(date, new WeatherInfo(date, temp, description));
                        perDay.add(grouped.get(date));
                    }
                }

                WeatherInfo today = perDay.isEmpty() ? null : perDay.get(0);
                callback.onSuccess(today, perDay);
            } catch (Exception e) {
                callback.onError("Не удалось загрузить погоду. Проверьте API-ключ.");
            }
        });
    }
}
