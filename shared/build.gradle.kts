plugins {
    id("junjange.kotlin.multiplatform")
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
            export(project(":core:domain"))
            export(project(":core:data"))
            export(project(":core:remote"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:data"))
            api(project(":core:remote"))
        }
    }
}
