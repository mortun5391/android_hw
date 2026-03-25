package com.example.extra_1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class WeatherViewModel extends ViewModel {

    private final MutableLiveData<WeatherInfo> todayWeather = new MutableLiveData<>();
    private final MutableLiveData<List<WeatherInfo>> weekWeather = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<WeatherInfo> getTodayWeather() { return todayWeather; }
    public LiveData<List<WeatherInfo>> getWeekWeather() { return weekWeather; }
    public LiveData<String> getError() { return error; }

    public void loadWeather(String city) {
        WeatherRepository.getInstance().loadWeather(city, new WeatherRepository.Callback() {
            @Override
            public void onSuccess(WeatherInfo today, List<WeatherInfo> week) {
                todayWeather.postValue(today);
                weekWeather.postValue(week);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
            }
        });
    }
}
