plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.ericafenyo.quickheadline"

  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.ericafenyo.quickheadline"
    minSdk = libs.versions.minSdk.get().toInt()
    versionCode = 2
    versionName = "1.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables.useSupportLibrary = true

    //TODO: Add your Google Places API here
    //https://developers.google.com/places/android-api/
//    manifestPlaceholders = [placeApiKey: "YOUR_KEY_HERE"]
  }

//  buildConfigField "String", 'NEWS_API_KEY', newsApi
//  buildConfigField "String", 'WEATHER_API_KEY', weatherApi

  signingConfigs {
    create("release") {
      storeFile = file("$rootDir/keystore.jks")
      storePassword = "2d68b6d49428a3dc"
      keyAlias = "capstone_project_udacity_key"
      keyPassword = "2d68b6d49428a3dc"
    }
  }

  buildTypes {
    debug {
      isMinifyEnabled = false
      versionNameSuffix = "-DEBUG"
    }

    release {
      signingConfig = signingConfigs.getByName("release")
      isShrinkResources = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  buildFeatures {
    dataBinding = true
    buildConfig = true
  }
}

dependencies {
  implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
  coreLibraryDesugaring(libs.desugarJdk8)

  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.material)
  implementation(libs.androidx.constraintlayout)

//  implementation(libs.androidx.browser)

//  implementation(libs.androidx.core)

//  implementation(libs.androidx.splashscreen)
  implementation(libs.androidx.preference)

  implementation(libs.gson)

  implementation(libs.retrofit2.retrofit)
  implementation(libs.retrofit2.gsonConverter)

  implementation(libs.timber)

  implementation(libs.kotlin.stdlib)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.databinding)

  implementation(libs.coil)

  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)

  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.fragment)

  implementation(libs.androidx.lifecycle.livedata)

  implementation(libs.androidx.fragment)

  implementation(libs.leakCanary)
}