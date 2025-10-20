plugins {
    id("junjange.core.module")
    alias(libs.plugins.parcelize)
}

android {
    namespace = "junjange.core.kakao"
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

    implementation(libs.bundles.common)
    implementation(libs.junit.ktx)

    // ksp
    ksp(libs.ksp.hilt)

    // kakao
    implementation(libs.kakao.user)
}
