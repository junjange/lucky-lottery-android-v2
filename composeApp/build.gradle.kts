import java.io.FileInputStream
import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val adMobAppId = localProperties.getProperty("AD_MOB_APP_ID") ?: ""
val fullScreenAdUnitId = localProperties.getProperty("FULL_SCREEN_AD_UNIT_ID") ?: "\"\""

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "LuckyLotteryShared"
            isStatic = true
            // Export all transitive dependencies so iOS can link them
            transitiveExport = true
            export(projects.core.domain)
            export(projects.core.data)
            export(projects.core.remote)
            export(projects.core.ui)
            export(projects.feature.splash)
            export(projects.feature.home)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.coroutines.core)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            api(projects.core.domain)
            api(projects.core.data)
            api(projects.core.remote)
            api(projects.core.ui)
            api(libs.navigation.compose.multiplatform)
            implementation(projects.core.local)
            implementation(projects.core.ocr)
            api(projects.feature.splash)
            api(projects.feature.home)
            implementation(projects.feature.mynumber)
            implementation(projects.feature.notification)
            implementation(projects.feature.randomnumber)
            implementation(projects.feature.randomnumbergeneration)
            implementation(projects.feature.setting)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose.multiplatform)
            implementation(libs.activity.compose.versioned)
            implementation(libs.compose.navigation)
            implementation(libs.compose.lifecycle)
            implementation(libs.bundles.android)
            implementation(libs.bundles.workmanager)
            implementation(libs.koin.annotations)
            implementation(libs.google.admob.ads)
        }
        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.koin.test)
            implementation(libs.koin.test.junit4)
            implementation(libs.ktor.client.core)
            implementation(libs.tesseract.android)
        }
    }
}

android {
    namespace = "com.junjange.lotto3"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.junjange.lotto3"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.compile.sdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        buildConfigField("String", "FULL_SCREEN_AD_UNIT_ID", fullScreenAdUnitId)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            resValue("string", "AD_MOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")
        }
        release {
            resValue("string", "AD_MOB_APP_ID", adMobAppId)
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
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Koin KSP Compiler for compile-time verification
    add("kspAndroid", libs.koin.compiler)

    debugImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling.debug)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.junit.espresso)
    androidTestImplementation(platform(libs.compose.bom))
}
