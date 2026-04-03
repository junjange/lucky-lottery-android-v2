import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktorfit)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: "\"\""

android {
    namespace = "junjange.core.firebase"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

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
}
dependencies {
    implementation(projects.core.data)
    implementation(projects.core.local)

    implementation(libs.bundles.common)
    implementation(libs.bundles.google)
    implementation(libs.bundles.network)
    implementation(libs.kotlinx.serialization)

    // Ktorfit KSP processor
    ksp(libs.ktorfit.ksp)
}
