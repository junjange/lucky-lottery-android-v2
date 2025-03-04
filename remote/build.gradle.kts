plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(Modules.DATA))

    implementation(libs.coroutines.core)

    implementation(libs.hilt.core)
    implementation(libs.jsoup.jsoup)

    // ksp
    ksp(libs.ksp.hilt)

    // network
    implementation(libs.bundles.network)
}
