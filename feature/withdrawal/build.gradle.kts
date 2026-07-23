import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.compose.multiplatform")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: "\"\""
val googleClientSecret = localProperties.getProperty("GOOGLE_CLIENT_SECRET") ?: "\"\""

android {
    namespace = "junjange.feature.withdrawal"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
        buildConfigField("String", "GOOGLE_CLIENT_SECRET", googleClientSecret)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "junjange.feature.withdrawal.resources"
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
            implementation(projects.core.firebase)
            implementation(libs.bundles.google)
        }
    }
}
