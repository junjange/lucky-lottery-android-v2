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
    compileOnly(libs.kotlinx.serialization)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "junjange.android.application"
            implementationClass = "junjange.build_logic.convention.AndroidApplicationConventionPlugin"
        }
        create("androidLibrary") {
            id = "junjange.android.library"
            implementationClass = "junjange.build_logic.convention.AndroidLibraryConventionPlugin"
        }
        create("coreModule") {
            id = "junjange.core.module"
            implementationClass = "junjange.build_logic.convention.CoreModuleConventionPlugin"
        }
        create("featureModule") {
            id = "junjange.feature.module"
            implementationClass = "junjange.build_logic.convention.FeatureModuleConventionPlugin"
        }
        create("kotlinJvm") {
            id = "junjange.kotlin.jvm"
            implementationClass = "junjange.build_logic.convention.KotlinJvmConventionPlugin"
        }
    }
}
