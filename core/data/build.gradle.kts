plugins {
    id("junjange.kotlin.jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(Modules.CORE_DOMAIN))

    implementation(libs.coroutines.core)
    implementation(libs.koin.core)

    // okhttp
    implementation(libs.okhttp.core)
}
