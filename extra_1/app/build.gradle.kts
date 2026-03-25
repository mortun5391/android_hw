import java.util.Properties

plugins { id("com.android.application") }

val localProps = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { load(it) }
    }
}

val openWeatherApiKey = (
    localProps.getProperty("OPEN_WEATHER_API_KEY")
        ?: project.findProperty("OPEN_WEATHER_API_KEY") as String?
        ?: System.getenv("OPEN_WEATHER_API_KEY")
        ?: ""
).trim()

android {
    namespace = "com.example.extra_1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.extra_1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$openWeatherApiKey\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}
