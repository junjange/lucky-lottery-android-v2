import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "junjange.build-logic"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.multiplatform.gradle.plugin)
    compileOnly(libs.kotlinx.serialization)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "junjange.android.application"
            implementationClass = "junjange.build_logic.convention.AndroidApplicationConventionPlugin"
        }
        create("kotlinMultiplatform") {
            id = "junjange.kotlin.multiplatform"
            implementationClass = "junjange.build_logic.convention.KotlinMultiplatformConventionPlugin"
        }
        create("kotlinMultiplatformLibrary") {
            id = "junjange.kotlin.multiplatform.library"
            implementationClass = "junjange.build_logic.convention.KmpLibraryConventionPlugin"
        }
        create("composeMultiplatform") {
            id = "junjange.compose.multiplatform"
            implementationClass = "junjange.build_logic.convention.ComposeMultiplatformConventionPlugin"
        }
    }
}
