import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.feature.module")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val fullScreenAdUnitId = localProperties.getProperty("FULL_SCREEN_AD_UNIT_ID") ?: ""
val bannerAdUnitId = localProperties.getProperty("BANNER_AD_UNIT_ID") ?: ""

android {
    namespace = "junjange.feature.main"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

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
}

dependencies {
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_UI))
    implementation(project(Modules.CORE_DESIGNSYSTEM))
    implementation(project(Modules.CORE_NAVIGATION))
    implementation(project(Modules.FEATURE_HOME))
    implementation(project(Modules.FEATURE_MYNUMBER))
    implementation(project(Modules.FEATURE_SETTING))

    implementation(platform(libs.compose.bom))
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
