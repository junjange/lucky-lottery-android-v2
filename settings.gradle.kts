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
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = uri("https://jitpack.io") }
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Lotto3"
include(":app")
include(":shared")
include(":core:domain")
include(":core:data")
include(":core:kakao")
include(":core:firebase")
include(":core:local")
include(":core:ocr")
include(":core:remote")
include(":core:ui")
include(":core:navigation")
include(":feature:main")
include(":feature:editprofile")
include(":feature:home")
include(":feature:login")
include(":feature:my")
include(":feature:mynumber")
include(":feature:notification")
include(":feature:randomnumber")
include(":feature:randomnumbergeneration")
include(":feature:register")
include(":feature:splash")
include(":feature:withdrawal")
include(":feature:setting")
