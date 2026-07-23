plugins {
    id("junjange.compose.multiplatform")
}

android {
    namespace = "junjange.shared"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
}

kotlin {
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
            export(project(":core:domain"))
            export(project(":core:data"))
            export(project(":core:remote"))
            export(project(":core:ui"))
            export(project(":feature:splash"))
            export(project(":feature:home"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:data"))
            api(project(":core:remote"))
            api(project(":core:ui"))
            api(project(":core:navigation"))
            api(libs.navigation.compose.multiplatform)
            api(libs.koin.compose)
            implementation(project(":core:local"))
            implementation(project(":core:ocr"))
            api(project(":feature:splash"))
            api(project(":feature:home"))
            implementation(project(":feature:mynumber"))
            implementation(project(":feature:notification"))
            implementation(project(":feature:randomnumber"))
            implementation(project(":feature:randomnumbergeneration"))
            implementation(project(":feature:setting"))
            implementation(project(":feature:main"))
        }
    }
}
