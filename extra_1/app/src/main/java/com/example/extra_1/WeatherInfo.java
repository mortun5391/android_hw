package com.example.extra_1;

public class WeatherInfo {
    private final String day;
    private final double tempCelsius;
    private final String description;

    public WeatherInfo(String day, double tempCelsius, String description) {
        this.day = day;
        this.tempCelsius = tempCelsius;
        this.description = description;
    }

    public String getDay() { return day; }
    public double getTempCelsius() { return tempCelsius; }
    public String getDescription() { return description; }
}
