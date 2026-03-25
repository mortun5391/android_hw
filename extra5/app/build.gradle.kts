plugins { id("com.android.application"); id("org.jetbrains.kotlin.android"); id("org.jetbrains.kotlin.kapt") }
android { namespace = "com.example.extra5"; compileSdk = 34
 defaultConfig { applicationId = "com.example.extra5"; minSdk = 24; targetSdk = 34; versionCode = 1; versionName = "1.0" }
 buildFeatures { viewBinding = true }
}
dependencies {
 implementation("androidx.core:core-ktx:1.13.1")
 implementation("androidx.appcompat:appcompat:1.7.1")
 implementation("androidx.recyclerview:recyclerview:1.3.2")
 implementation("androidx.room:room-runtime:2.6.1")
 implementation("androidx.room:room-ktx:2.6.1")
 kapt("androidx.room:room-compiler:2.6.1")
 implementation("com.github.bumptech.glide:glide:4.16.0")
 kapt("com.github.bumptech.glide:compiler:4.16.0")
 implementation("com.squareup.retrofit2:retrofit:2.11.0")
 implementation("com.squareup.retrofit2:converter-gson:2.11.0")
 implementation("com.google.android.material:material:1.12.0")
}
