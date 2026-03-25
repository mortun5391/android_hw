# extra_1 (Weather)

## OpenWeather API key setup

1. Open `extra_1/local.properties` and add:

```properties
OPEN_WEATHER_API_KEY=your_openweather_api_key
```

2. **Sync Gradle** ("Sync Project with Gradle Files").
3. **Rebuild + reinstall** the app (BuildConfig is generated at build time).

Alternative: set environment variable before build:

```bash
export OPEN_WEATHER_API_KEY=your_openweather_api_key
```

The app reads this value into `BuildConfig.OPEN_WEATHER_API_KEY`.
If it is missing or invalid, the main screen shows a clear message.
