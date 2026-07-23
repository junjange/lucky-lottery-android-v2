plugins {
    id("junjange.compose.multiplatform")
}

android {
    namespace = "junjange.feature.randomnumbergeneration"
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
    packageOfResClass = "junjange.feature.randomnumbergeneration.resources"
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
            implementation(libs.bundles.google)
        }
    }
}
