plugins {
    id("junjange.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            implementation(libs.ktor.client.core)
        }
    }
}
