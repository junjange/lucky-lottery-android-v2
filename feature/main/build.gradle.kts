import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.compose.multiplatform")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val fullScreenAdUnitId = localProperties.getProperty("FULL_SCREEN_AD_UNIT_ID") ?: "\"\""
val bannerAdUnitId = localProperties.getProperty("BANNER_AD_UNIT_ID") ?: "\"\""

android {
    namespace = "junjange.feature.main"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        buildConfigField("String", "FULL_SCREEN_AD_UNIT_ID", fullScreenAdUnitId)
        buildConfigField("String", "BANNER_AD_UNIT_ID", bannerAdUnitId)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "junjange.feature.main.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(projects.core.navigation)
            implementation(projects.feature.home)
            implementation(projects.feature.mynumber)
            implementation(projects.feature.setting)
        }
        androidMain.dependencies {
            implementation(libs.bundles.google)
            implementation(libs.bundles.datastore)
            implementation(libs.zxing.android.embedded)
            implementation(libs.zxing.core)
        }
    }
}
