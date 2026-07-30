plugins {
    id("junjange.compose.multiplatform")
}

android {
    namespace = "junjange.feature.mynumber"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "junjange.feature.mynumber.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(projects.core.ocr)
            // 번호 담기 화면·삭제 모드에서 시스템 뒤로 가기를 가로챈다.
            implementation(libs.compose.ui.backhandler)
        }
        androidMain.dependencies {
            implementation(libs.image.cropper)
        }
    }
}
