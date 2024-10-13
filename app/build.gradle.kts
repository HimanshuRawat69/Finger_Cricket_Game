plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

}



android {
    namespace = "com.example.fingurecricketgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fingurecricketgame"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dexOptions {
        javaMaxHeapSize ="4g"  // You can adjust the heap size value as needed (e.g., "2g", "3g", etc.)
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.airbnb.android:lottie:6.3.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2") // Use the latest version
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.22")


}