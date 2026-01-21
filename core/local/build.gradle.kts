plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.core.local"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    // Java/Kotlin options provided by convention plugin
}
dependencies {
    implementation(project(Modules.CORE_DATA))
    implementation(project(Modules.CORE_NOTIFICATION))

    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.common)
    implementation(libs.bundles.room)
    implementation(libs.junit.ktx)

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
}
