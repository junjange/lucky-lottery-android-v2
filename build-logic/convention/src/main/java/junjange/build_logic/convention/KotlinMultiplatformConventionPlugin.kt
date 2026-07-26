package junjange.build_logic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                jvm()
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
