import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: ""
val googleClientSecret = localProperties.getProperty("GOOGLE_CLIENT_SECRET") ?: ""
val fullScreenAdUnitId = localProperties.getProperty("FULL_SCREEN_AD_UNIT_ID") ?: ""
val bannerAdUnitId = localProperties.getProperty("BANNER_AD_UNIT_ID") ?: ""

android {
    namespace = "com.junjange.presentation"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
        buildConfigField("String", "GOOGLE_CLIENT_SECRET", googleClientSecret)
        buildConfigField("String", "FULL_SCREEN_AD_UNIT_ID", fullScreenAdUnitId)
        buildConfigField("String", "BANNER_AD_UNIT_ID", bannerAdUnitId)
    }

    buildTypes {
        release {
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.versions.compose.compiler
                .get()
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))

    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.common)

    // ksp
    ksp(libs.ksp.hilt)

    // google
    implementation(libs.bundles.google)

    // datastore
    implementation(libs.bundles.datastore)

    // test
    testImplementation(libs.junit)

    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.junit.espresso)

    debugImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling.debug)

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
}
