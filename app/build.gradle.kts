plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.kamus.cookit"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.kamus.cookit"
        minSdk = 24
        targetSdk = 33
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

    buildFeatures{
        viewBinding=true
    }
}

dependencies {
    val roomVersion: String = "2.6.0-beta01"
    val splashScreenVersion: String = "1.0.1"
    val lifecycleVersion: String = "2.6.2"
    val retrofitVersion: String = "2.9.0"
    val glideVersion: String = "4.15.1"
    val roundedImagesVersion: String = "2.3.0"
    val carouselRecyclerViewVersion: String = "1.2.6"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:$splashScreenVersion")

    // Room
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Retrofit, Gson
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")

    // Rounded images
    implementation("com.makeramen:roundedimageview:$roundedImagesVersion")

    // Carousel recyclerview
    implementation("com.github.sparrow007:carouselrecyclerview:$carouselRecyclerViewVersion")

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}