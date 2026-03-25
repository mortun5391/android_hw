# extra1 (Weather)

## OpenWeather API key setup

Create or edit `local.properties` in `extra1/` and add:

```properties
OPEN_WEATHER_API_KEY=your_openweather_api_key
```

Alternative: export environment variable before Gradle build:

```bash
export OPEN_WEATHER_API_KEY=your_openweather_api_key
```

The app reads this value into `BuildConfig.OPEN_WEATHER_API_KEY`.
If it is missing or invalid, you will see a clear message in the UI.
