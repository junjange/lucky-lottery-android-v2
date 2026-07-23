plugins {
    id("junjange.compose.multiplatform")
}

android {
    namespace = "junjange.feature.login"
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
    packageOfResClass = "junjange.feature.login.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(projects.core.navigation)
            implementation(projects.core.firebase)
        }
        androidMain.dependencies {
            implementation(libs.bundles.google)
            implementation(libs.bundles.datastore)
        }
    }
}
