import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: ""

android {
    namespace = "junjange.core.google"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        buildConfig = true
    }
    // Java/Kotlin options provided by convention plugin
}
dependencies {
    implementation(libs.bundles.common)

    // google
    implementation(libs.bundles.google)

    // ksp
    ksp(libs.ksp.hilt)
}
