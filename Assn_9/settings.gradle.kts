@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        // Order: plugin portal, Google, Maven Central
        gradlePluginPortal()
        google {
            // (Optional) keep filters if you like, but DO NOT put other repos inside content{}
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

dependencyResolutionManagement {
    // This forbids repos in module/build files (app/build.gradle.kts)
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Assn_9"
include(":app")
