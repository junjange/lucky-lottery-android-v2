import java.io.FileInputStream
import java.util.Properties

plugins {
    id("junjange.kotlin.multiplatform.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: "\"\""

android {
    namespace = "junjange.core.firebase"

    defaultConfig {
        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:data"))

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktorfit.lib)
        }

        androidMain.dependencies {
            implementation(project(":core:local"))
            implementation(libs.koin.android)
            implementation(libs.core.ktx)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.bundles.google)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspIosX64", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
}
