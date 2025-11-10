plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)   // ✅ keep just the alias; remove the extra explicit id
}

android {
    namespace = "com.example.assn_9"

    // ✅ compileSdk must be an Int, not a nested block
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.assn_9"
        minSdk = 26
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

    // ✅ Java/Kotlin target
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }

    // ✅ enable Compose once (you had it twice)
    buildFeatures { compose = true }

    // If you want to pin the Compose compiler extension manually, you can add:
    // composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }
}

dependencies {
    // --- Compose BOM (one time) ---
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))

    // --- Compose + AndroidX ---
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // --- Retrofit + Gson + OkHttp ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- Coil (choose ONE line of Coil; here we pick v2 to match your current code) ---
    implementation("io.coil-kt:coil-compose:2.7.0")
    // ^ If you later switch to Coil v3, REMOVE this line and add:
    // implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    // implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
}
