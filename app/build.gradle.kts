plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.gymappsas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gymappsas"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    dependencies {
        // Room for database persistence

        implementation(libs.androidx.room.runtime)
        annotationProcessor(libs.androidx.room.compiler)

        // To use Kotlin Symbol Processing (KSP)
        ksp(libs.androidx.room.compiler)

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation(libs.androidx.room.ktx)
        // AndroidX libraries
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)

        // Material Design libraries
        implementation(libs.material)
        implementation(libs.androidx.material3)

        // Navigation libraries for fragments and UI
        implementation(libs.androidx.navigation.ui.ktx)
        implementation(libs.androidx.navigation.fragment)

        // Hilt integration for Compose
        implementation(libs.androidx.hilt.navigation.compose)

        // Compose libraries
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)

        // Compose-specific testing
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        // Networking library
        implementation(libs.nanohttpd)

        // JSON converter
        implementation(libs.converter.gson)

        // Additional third-party libraries
        implementation(libs.view) // Calendar view library for Android
        implementation(libs.compose) // Calendar Compose library for Android
        implementation(libs.swipe) // Swipe-to-refresh
        implementation(libs.coil.compose) // Image loading with Coil

        // Safe Args for Navigation
        //implementation(libs.androidx.navigation.safe.args.gradle.plugin)

        // Testing dependencies
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        testImplementation(libs.junit)
        implementation(libs.hilt.android.v250)
        ksp(libs.hilt.compiler)

        //storage
        implementation(libs.androidx.storage)

        //cameraX
        // The following line is optional, as the core library is included indirectly by camera-camera2
        implementation(libs.androidx.camera.core)
        implementation(libs.androidx.camera.camera2)
        // If you want to additionally use the CameraX Lifecycle library
        implementation(libs.androidx.camera.lifecycle)
        // If you want to additionally use the CameraX VideoCapture library
        implementation(libs.androidx.camera.video)
        // If you want to additionally use the CameraX View class
        implementation(libs.androidx.camera.view)
        // If you want to additionally add CameraX ML Kit Vision Integration
        implementation(libs.androidx.camera.mlkit.vision)
        // If you want to additionally use the CameraX Extensions library
        implementation(libs.androidx.camera.extensions)
        implementation(libs.accompanist.permissions)

        //coil
        implementation(libs.coil.compose)
        implementation(libs.coil.network.okhttp)

        //android media
        implementation(libs.androidx.media)
    }
}
dependencies {
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.webkit)
    implementation(libs.androidx.fragment.compose)
}
