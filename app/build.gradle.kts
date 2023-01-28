plugins {
    kotlin("android") version "1.8.0"
    id("com.android.application")
}

android {
    namespace = "com.samsung.SMT.lang.exploit"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        @Suppress("UnstableApiUsage")
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.0")
}
