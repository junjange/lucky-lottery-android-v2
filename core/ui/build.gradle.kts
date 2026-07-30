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
    namespace = "junjange.core.ui"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

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
    buildFeatures {
        buildConfig = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "junjange.core.ui.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            api(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
            // 번호 담기 화면이 시스템 뒤로 가기(iOS는 왼쪽 엣지 스와이프)를 직접 받는다.
            implementation(libs.compose.ui.backhandler)
        }
        androidMain.dependencies {
            implementation(libs.google.admob.ads)
            implementation(libs.core.ktx)
            implementation(libs.compose.navigation)
        }
    }
}
