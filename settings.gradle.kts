pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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

rootProject.name = "Lotto3"
include(":app")
include(":core:domain")
include(":core:data")
include(":presentation")
include(":core:kakao")
include(":core:firebase")
include(":core:local")
include(":core:ocr")
include(":core:remote")
include(":core:ui")
include(":core:designsystem")
include(":core:navigation")
