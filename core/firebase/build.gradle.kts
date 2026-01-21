plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktorfit)
}

android {
    namespace = "junjange.core.firebase"
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
    // Java/Kotlin options are provided by convention plugin
}
dependencies {
    implementation(project(Modules.FEATURE_MAIN))
    implementation(project(Modules.CORE_DATA))

    implementation(libs.bundles.common)
    implementation(libs.bundles.google)
    implementation(libs.bundles.network)
    implementation(libs.kotlinx.serialization)

    // Ktorfit KSP processor
    ksp(libs.ktorfit.ksp)
}
