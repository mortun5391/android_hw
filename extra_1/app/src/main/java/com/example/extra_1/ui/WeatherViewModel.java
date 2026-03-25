package com.example.extra_1.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.extra_1.api.NetworkSingleton;
import com.example.extra_1.model.WeatherModels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<String> today = new MutableLiveData<>();
    private final MutableLiveData<List<WeatherModels.DayForecast>> week = new MutableLiveData<>();

    public LiveData<String> getToday() { return today; }
    public LiveData<List<WeatherModels.DayForecast>> getWeek() { return week; }

    public void load(String city, String apiKey) {
        NetworkSingleton.getWeatherApi().getForecast(city, apiKey, "metric", "ru")
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<WeatherModels.ForecastResponse> call, Response<WeatherModels.ForecastResponse> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                today.setValue("Не удалось загрузить погоду. Проверьте API-ключ.");
                            } else {
                                today.setValue("Ошибка сервера: " + response.code());
                            }
                            week.setValue(new ArrayList<>());
                            return;
                        }

                        List<WeatherModels.ForecastItem> data = response.body() != null ? response.body().list : new ArrayList<>();
                        if (data == null || data.isEmpty()) {
                            today.setValue("Нет данных о погоде.");
                            week.setValue(new ArrayList<>());
                            return;
                        }

                        WeatherModels.ForecastItem first = data.get(0);
                        String desc = (first.weather != null && !first.weather.isEmpty()) ? first.weather.get(0).description : "";
                        today.setValue(first.main.temp + "°C, " + desc);

                        Map<String, List<Double>> byDay = new LinkedHashMap<>();
                        for (WeatherModels.ForecastItem item : data) {
                            String day = item.dt_txt.split(" ")[0];
                            byDay.computeIfAbsent(day, k -> new ArrayList<>()).add(item.main.temp);
                        }

                        List<WeatherModels.DayForecast> out = new ArrayList<>();
                        int i = 0;
                        for (Map.Entry<String, List<Double>> e : byDay.entrySet()) {
                            if (i++ >= 7) break;
                            double sum = 0;
                            for (Double t : e.getValue()) sum += t;
                            out.add(new WeatherModels.DayForecast(e.getKey(), ((int) (sum / e.getValue().size())) + "°C"));
                        }
                        week.setValue(out);
                    }

                    @Override
                    public void onFailure(Call<WeatherModels.ForecastResponse> call, Throwable t) {
                        today.setValue("Ошибка сети: " + t.getMessage());
                        week.setValue(new ArrayList<>());
                    }
                });
    }
}
