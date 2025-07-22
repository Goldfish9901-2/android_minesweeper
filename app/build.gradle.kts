plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // 启用 Kotlin 插件
//    kotlin("kapt")
}

android {
    namespace = "org.goldfish.minesweeper_android_01"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.goldfish.minesweeper_android_01"
        minSdk = 29
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

    lint {
        checkReleaseBuilds = false
    }
}


dependencies {
    // Android 基础库
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.material.v1120)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // 测试
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 第三方库
    implementation(libs.smarttable)
    implementation(libs.androidx.monitor)

    // Room 数据库
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.androidx.room.room.rxjava22)
    implementation(libs.androidx.room.room.rxjava32)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)

    // Lombok 配置
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.room.compiler)

    // Lombok + Kotlin + kapt 支持
//    kapt("org.projectlombok:lombok-mapstruct-binding:0.2.0")
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}
