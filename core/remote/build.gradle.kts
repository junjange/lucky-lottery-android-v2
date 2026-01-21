plugins {
    id("junjange.kotlin.jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(Modules.CORE_DATA))

    implementation(libs.coroutines.core)
    implementation(libs.koin.core)

    // network
    implementation(libs.bundles.network)
}
