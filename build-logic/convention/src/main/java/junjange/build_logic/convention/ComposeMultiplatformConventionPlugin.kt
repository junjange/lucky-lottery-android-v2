package junjange.build_logic.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("compile-sdk").get().toString().toInt()
                defaultConfig {
                    minSdk = libs.findVersion("min-sdk").get().toString().toInt()
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
            }

            val composeExt = extensions.getByType(ComposeExtension::class.java)

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_1_8)
                    }
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()

                applyDefaultHierarchyTemplate()

                sourceSets.commonMain.dependencies {
                    implementation(composeExt.dependencies.runtime)
                    implementation(composeExt.dependencies.foundation)
                    implementation(composeExt.dependencies.material3)
                    implementation(composeExt.dependencies.ui)
                    implementation(composeExt.dependencies.components.resources)
                    implementation(composeExt.dependencies.materialIconsExtended)
                    implementation(libs.findLibrary("coroutines-core").get())
                    implementation(libs.findLibrary("kotlinx-serialization").get())
                    implementation(libs.findLibrary("kotlinx-datetime").get())
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("koin-compose").get())
                }

                sourceSets.androidMain.dependencies {
                    implementation(libs.findLibrary("koin-android").get())
                    implementation(libs.findLibrary("koin-androidx-compose").get())
                    implementation(libs.findLibrary("koin-compose-viewmodel").get())
                    implementation(libs.findLibrary("lifecycle-viewmodel-compose-multiplatform").get())
                    implementation(libs.findLibrary("activity-compose-versioned").get())
                    implementation(libs.findLibrary("compose-material3").get())
                    implementation(libs.findLibrary("compose-material").get())
                    implementation(libs.findLibrary("compose-navigation").get())
                    implementation(libs.findLibrary("compose-lifecycle").get())
                    implementation(libs.findLibrary("core-ktx").get())
                    implementation(libs.findLibrary("coil").get())
                }
            }
        }
    }
}
