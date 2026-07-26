plugins {
    id("junjange.compose.multiplatform")
}

android {
    namespace = "junjange.core.navigation"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            implementation(libs.navigation.compose.multiplatform)
        }
    }
}
