plugins {
    id("junjange.kotlin.jvm")
}

dependencies {
    implementation(libs.koin.core)

    // Ktor for multipart
    implementation(libs.ktor.client.core)
}
