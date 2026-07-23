plugins {
    id("junjange.compose.multiplatform")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.feature.mynumber"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "junjange.feature.mynumber.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(projects.core.navigation)
        }
        androidMain.dependencies {
            implementation(projects.core.ocr)
            implementation(libs.paging.compose)
            implementation(libs.image.cropper)
        }
    }
}
