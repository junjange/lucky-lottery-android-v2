plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.core.designsystem"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
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
    // Java/Kotlin options provided by convention plugin
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.common)

    // google
    implementation(libs.bundles.google)

    debugImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling.debug)

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
}
