plugins {
    id("junjange.feature.module")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.feature.splash"
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
    }
}

dependencies {
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_UI))
    implementation(project(Modules.CORE_DESIGNSYSTEM))
    implementation(project(Modules.CORE_NAVIGATION))

    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.common)

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
