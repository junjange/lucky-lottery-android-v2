plugins {
    id("junjange.kotlin.jvm")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktorfit)
}

dependencies {
    implementation(project(Modules.CORE_DATA))

    implementation(libs.coroutines.core)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization)

    // network - Ktor + Ktorfit
    implementation(libs.bundles.network)
    ksp(libs.ktorfit.ksp)
}
