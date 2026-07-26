pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Lotto3"
include(":composeApp")
include(":core:domain")
include(":core:data")
include(":core:local")
include(":core:ocr")
include(":core:remote")
include(":core:ui")
include(":feature:home")
include(":feature:mynumber")
include(":feature:notification")
include(":feature:randomnumber")
include(":feature:randomnumbergeneration")
include(":feature:splash")
include(":feature:setting")
