plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.core.ocr"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

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
    // Java/Kotlin options provided by convention plugin
}
dependencies {
    implementation(libs.bundles.common)

    implementation(libs.tesseract.android)
}
