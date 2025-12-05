pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        // Additional mirror to mitigate transient Maven Central outages (e.g., Groovy artifacts)
        maven {
            url = uri("https://repo.grails.org/grails/core")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

@Suppress("UnstableApiUsage") dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Additional mirror to mitigate transient Maven Central outages (e.g., Groovy artifacts)
        maven {
            url = uri("https://repo.grails.org/grails/core")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "App Toolkit for Android"
include(":app")
include(":apptoolkit")