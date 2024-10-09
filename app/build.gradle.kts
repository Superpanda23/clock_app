plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "re.recodestudios.clock_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "re.recodestudios.clock_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.datetime)
    coreLibraryDesugaring (libs.desugar.jdk.libs)

    // Compose dependencies
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation ("androidx.navigation:navigation-compose:2.8.2")
    implementation ("androidx.compose.material:material-icons-extended:1.7.3")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    ksp ("com.google.dagger:dagger-compiler:2.48")

    ksp ("com.google.dagger:hilt-compiler:2.48")
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Room
    implementation(libs.room)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Or latest version
    testImplementation ("io.mockk:mockk:1.13.9") // Or latest version


}