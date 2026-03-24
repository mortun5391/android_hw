plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.khalilbek.hw8"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.khalilbek.hw8"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Map library (Osmdroid for OpenStreetMap)
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    
    // Room for local database
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    
    // RecyclerView for chat messages
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // SharedPreferences helper
    implementation("androidx.preference:preference:1.2.1")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}