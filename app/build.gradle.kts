plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.compose.compiler) // apply false

    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
}

android {
    namespace = "com.example.cryptoviewer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cryptoviewer"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // view model
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata.compose)
    // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")

    // gson for json parsing
    // implementation("com.google.code.gson:gson:2.8.9")

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    // room
    // val room_version = "2.6.1"
    // implementation("androidx.room:room-runtime:$room_version")
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    // ksp(libs.androidx.room.compiler) // for annotation processing // ksp or kapt?
    implementation(libs.androidx.room.ktx) // Kotlin Extensions and Coroutines support for Room

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
