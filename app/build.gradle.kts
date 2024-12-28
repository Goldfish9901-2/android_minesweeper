plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.goldfish.minesweeper_android_01"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.goldfish.minesweeper_android_01"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
dependencies{
    implementation(files("src/main/libs/AMap3DMap_10.0.900_AMapSearch_9.7.3_AMapLocation_6.4.7_20240816.jar"))
    implementation(files("src/main/libs/AMap3DMap_10.0.900_AMapSearch_9.7.3_AMapLocation_6.4.7_20240816.jar"))
    implementation(files("src/main/libs/AMap3DMap_10.0.900_AMapSearch_9.7.3_AMapLocation_6.4.7_20240816.jar"))
}
dependencies {

    implementation(libs.room.runtime)

//    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
//    // See Add the KSP plugin to your project
//    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor(libs.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    // optional - RxJava2 support for Room
    implementation(libs.androidx.room.room.rxjava22)

    // optional - RxJava3 support for Room
    implementation(libs.androidx.room.room.rxjava32)

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.room.paging)
}