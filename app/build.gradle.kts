import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.android.application")
    id("com.google.gms.google-services")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val baseUrl = localProperties.getProperty("BASE_URL") ?: ""
val kakaoNativeAppKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
val kakaoOauthHost = localProperties.getProperty("KAKAO_OAUTH_HOST") ?: ""
val adMobAppId = localProperties.getProperty("AD_MOB_APP_ID") ?: ""

android {
    namespace = "com.junjange.lotto3"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.junjange.lotto3"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.COMPILE_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

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
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_DATA))
    implementation(project(Modules.CORE_KAKAO))
    implementation(project(Modules.CORE_FIREBASE))
    implementation(project(Modules.CORE_LOCAL))
    implementation(project(Modules.CORE_OCR))
    implementation(project(Modules.CORE_REMOTE))
    implementation(project(Modules.CORE_UI))
    implementation(project(Modules.CORE_DESIGNSYSTEM))
    implementation(project(Modules.CORE_NAVIGATION))
    implementation(project(Modules.CORE_GOOGLE))
    implementation(project(Modules.CORE_NOTIFICATION))
    implementation(project(Modules.FEATURE_MAIN))
    implementation(project(Modules.FEATURE_EDITPROFILE))
    implementation(project(Modules.FEATURE_HOME))
    implementation(project(Modules.FEATURE_LOGIN))
    implementation(project(Modules.FEATURE_MY))
    implementation(project(Modules.FEATURE_MYNUMBER))
    implementation(project(Modules.FEATURE_NOTIFICATION))
    implementation(project(Modules.FEATURE_RANDOMNUMBER))
    implementation(project(Modules.FEATURE_RANDOMNUMBERGENERATION))
    implementation(project(Modules.FEATURE_REGISTER))
    implementation(project(Modules.FEATURE_SPLASH))
    implementation(project(Modules.FEATURE_WITHDRAWAL))
    implementation(project(Modules.FEATURE_SETTING))

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

    // okhttp
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

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
