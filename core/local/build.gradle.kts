plugins {
    id("junjange.kotlin.multiplatform.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "junjange.core.local"
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:data"))

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.multiplatform.settings)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.core.ktx)
            implementation(libs.work.runtime.ktx)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}
