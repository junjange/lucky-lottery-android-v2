plugins {
    id("junjange.kotlin.multiplatform.library")
}

android {
    namespace = "junjange.core.ocr"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.tesseract.android)
        }
    }
}
