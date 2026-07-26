package junjange.build_logic.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP library convention for non-Compose modules that still need a real Android target
 * (i.e. they use platform APIs such as Room, SharedPreferences, WorkManager on Android and
 * their iOS counterparts). Unlike [KotlinMultiplatformConventionPlugin] (jvm + ios) this
 * configures an `androidTarget` + the three iOS targets, without pulling in Compose.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
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
                    implementation(libs.findLibrary("coroutines-core").get())
                    implementation(libs.findLibrary("kotlinx-serialization").get())
                    implementation(libs.findLibrary("kotlinx-datetime").get())
                    implementation(libs.findLibrary("koin-core").get())
                }
            }
        }
    }
}
