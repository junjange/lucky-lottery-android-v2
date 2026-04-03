import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.android.application")
    id("com.google.gms.google-services")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val baseUrl = localProperties.getProperty("BASE_URL") ?: "\"\""
val kakaoNativeAppKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: "\"\""
val kakaoOauthHost = localProperties.getProperty("KAKAO_OAUTH_HOST") ?: ""
val adMobAppId = localProperties.getProperty("AD_MOB_APP_ID") ?: ""

android {
    namespace = "com.junjange.lotto3"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.junjange.lotto3"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.compile.sdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        buildConfigField("String", "BASE_URL", baseUrl)
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", kakaoNativeAppKey)
        resValue("string", "KAKAO_OAUTH_HOST", kakaoOauthHost)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            resValue("string", "AD_MOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")
        }
        release {
            resValue("string", "AD_MOB_APP_ID", adMobAppId)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.kakao)
    implementation(projects.core.firebase)
    implementation(projects.core.local)
    implementation(projects.core.ocr)
    implementation(projects.core.remote)
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.feature.main)
    implementation(projects.feature.editprofile)
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.my)
    implementation(projects.feature.mynumber)
    implementation(projects.feature.notification)
    implementation(projects.feature.randomnumber)
    implementation(projects.feature.randomnumbergeneration)
    implementation(projects.feature.register)
    implementation(projects.feature.splash)
    implementation(projects.feature.withdrawal)
    implementation(projects.feature.setting)

    implementation(libs.bundles.android)
    implementation(libs.bundles.common)
    implementation(libs.bundles.workmanager)
    implementation(libs.koin.annotations)

    // Koin KSP Compiler for compile-time verification
    ksp(libs.koin.compiler)

    // kakao
    implementation(libs.bundles.kakao)

    // google
    implementation(libs.bundles.google)

    // test
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.junit.espresso)
    debugImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling.debug)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
}
