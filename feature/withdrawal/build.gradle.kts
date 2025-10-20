import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.feature.module")
    alias(libs.plugins.parcelize)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: ""
val googleClientSecret = localProperties.getProperty("GOOGLE_CLIENT_SECRET") ?: ""

android {
    namespace = "junjange.feature.withdrawal"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
        buildConfigField("String", "GOOGLE_CLIENT_SECRET", googleClientSecret)
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
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_UI))
    implementation(project(Modules.CORE_DESIGNSYSTEM))
    implementation(project(Modules.CORE_NAVIGATION))
    implementation(project(Modules.CORE_GOOGLE))

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
