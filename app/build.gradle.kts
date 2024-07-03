plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "np.com.luminoussuwal.babybuy"
    compileSdk = 34

    defaultConfig {
        applicationId = "np.com.luminoussuwal.babybuy"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.play.services.cast.framework)
    implementation(libs.firebase.firestore.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val room_version = "2.6.1"

    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
   kapt ("androidx.room:room-compiler:$room_version")

    implementation ("com.github.bumptech.glide:glide:4.15.1") // Use the latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
   // implementation ("com.github.dhaval2404:imagepicker:2.1") // Replace with the latest version

    //for location
    implementation(libs.location)

    //for google maps
    implementation(libs.googleMaps)

    //for cameraX
    implementation(libs.cameraXCore)
    implementation(libs.camera2)
    implementation(libs.cameraLifeCycle)
    implementation(libs.cameraVideo)
    implementation(libs.cameraView)
    implementation(libs.cameraExtensions)

    implementation("com.google.guava:guava:31.1-android")


    // To use Kotlin annotation processing tool (kapt)
  //  kapt ("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
  //  ksp ("androidx.room:room-compiler:$room_version")
}