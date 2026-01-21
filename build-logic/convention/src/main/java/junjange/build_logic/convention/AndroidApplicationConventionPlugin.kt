package junjange.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = 35

                defaultConfig {
                    applicationId = "com.lucky.lottery"
                    minSdk = 24
                    targetSdk = 35
                    versionCode = 1
                    versionName = "1.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
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

                // Kotlin options are configured below via KotlinAndroidProjectExtension

                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.8"
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }

            val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

            // Configure Kotlin compiler options for Android modules
            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
                }
            }

            dependencies {
                add("implementation", libs.findBundle("android").get())
                add("implementation", libs.findBundle("kotlin").get())
                add("implementation", libs.findBundle("compose").get())
                add("implementation", libs.findBundle("common").get())
                add("implementation", libs.findBundle("google").get())
                add("implementation", libs.findBundle("kakao").get())
                add("implementation", libs.findBundle("network").get())
                add("implementation", libs.findBundle("datastore").get())
                add("implementation", libs.findBundle("room").get())

                add("ksp", libs.findLibrary("room-compiler").get())
            }
        }
    }
}
