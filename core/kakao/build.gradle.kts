plugins {
    id("junjange.kotlin.multiplatform.library")
}

android {
    namespace = "junjange.core.kakao"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:data"))
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.kakao.user)
        }
    }
}
