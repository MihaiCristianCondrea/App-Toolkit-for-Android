/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension
import java.util.Properties

plugins {
    alias(notation = libs.plugins.android.application)
    alias(notation = libs.plugins.kotlin.compose)
    alias(notation = libs.plugins.kotlin.parcelize)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.google.mobile.services) apply false
    alias(notation = libs.plugins.firebase.crashlytics) apply false
    alias(notation = libs.plugins.firebase.performance) apply false
    alias(notation = libs.plugins.about.libraries)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
}

/**
 * Released application identity. Firebase clients must match this value; changing it publishes
 * a different app.
 */
val releasedApplicationId = "com.d4rk.android.apps.apptoolkit"

val googleServicesFiles: List<File> = listOf(
    "google-services.json",
    "src/debug/google-services.json",
    "src/release/google-services.json",
).map(::file).filter(File::exists)

// Match generated JSON independently of whitespace, and enable Firebase only for the released application ID.
val packageNamePattern = Regex(
    """"package_name"\s*:\s*"${Regex.escape(releasedApplicationId)}""""
)
val hasMatchingGoogleServicesConfig: Boolean = googleServicesFiles.any { configFile ->
    packageNamePattern.containsMatchIn(configFile.readText())
}

val hasMismatchedGoogleServicesConfig: Boolean =
    googleServicesFiles.isNotEmpty() && !hasMatchingGoogleServicesConfig

if (hasMatchingGoogleServicesConfig) {
    apply(plugin = libs.plugins.google.mobile.services.get().pluginId)
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
    apply(plugin = libs.plugins.firebase.performance.get().pluginId)
} else if (hasMismatchedGoogleServicesConfig) {
    logger.warn(
        "google-services.json has no client for '$releasedApplicationId', so Firebase " +
                "(Analytics, Crashlytics, Performance) is disabled for this build. Add that package " +
                "in the Firebase console and re-download the file. See build-logic/README.md#application-id."
    )
}

// An absent local config is valid on CI; a present mismatched config must block release tasks.
gradle.taskGraph.whenReady {
    val assemblesRelease = allTasks.any { task ->
        task.project == project && task.name.contains("Release")
    }
    check(!hasMismatchedGoogleServicesConfig || !assemblesRelease) {
        "Refusing to build a release with a mismatched google-services.json: it has no client for " +
                "'$releasedApplicationId', so Crashlytics would be silently disabled. " +
                "See build-logic/README.md#application-id."
    }
}

val versioning = extensions.getByType<VersioningExtension>()
val appVersion = versioning.phoneVersion()

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit"
    compileSdk = appVersion.compileSdk

    defaultConfig {
        applicationId = releasedApplicationId
        resValue("string", "app_package_name", releasedApplicationId)
        minSdk = appVersion.minSdk
        targetSdk = appVersion.targetSdk
        versionCode = appVersion.versionCode
        versionName = appVersion.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        @Suppress("UnstableApiUsage")
        androidResources.localeFilters += listOf(
            "ar-rEG",
            "bg-rBG",
            "bn-rBD",
            "de-rDE",
            "en",
            "es-rGQ",
            "es-rMX",
            "fil-rPH",
            "fr-rFR",
            "hi-rIN",
            "hu-rHU",
            "in-rID",
            "it-rIT",
            "ja-rJP",
            "ko-rKR",
            "pl-rPL",
            "pt-rBR",
            "ro-rRO",
            "ru-rRU",
            "sv-rSE",
            "th-rTH",
            "tr-rTR",
            "uk-rUA",
            "ur-rPK",
            "vi-rVN",
            "zh-rTW"
        )
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true

        val githubProps = Properties()
        val githubFile = rootProject.file("github.properties")
        val githubToken = if (githubFile.exists()) {
            githubProps.load(githubFile.inputStream())
            githubProps["GITHUB_TOKEN"].toString()
        } else {
            ""
        }
        buildConfigField("String", "GITHUB_TOKEN", "\"$githubToken\"")
    }

    signingConfigs {
        create("release")

        val signingProps = Properties()
        val signingFile = rootProject.file("signing.properties")

        if (signingFile.exists()) {
            signingProps.load(signingFile.inputStream())

            signingConfigs.getByName("release").apply {
                storeFile = file(signingProps["STORE_FILE"].toString())
                storePassword = signingProps["STORE_PASSWORD"].toString()
                keyAlias = signingProps["KEY_ALIAS"].toString()
                keyPassword = signingProps["KEY_PASSWORD"].toString()
            }
        } else {
            android.buildTypes.getByName("release").signingConfig = null
        }
    }

    // Release signing is optional for local builds. AGP's unified optimization enables code and resource shrinking; Firebase mapping upload requires a matching host config.
    buildTypes {
        release {
            val signingFile = rootProject.file("signing.properties")
            signingConfig = if (signingFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                null
            }
            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
            optimization {
                enable = true
            }
            if (hasMatchingGoogleServicesConfig) {
                configure<CrashlyticsExtension> {
                    mappingFileUploadEnabled = true
                }
            }
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
        resValues = true
    }

    bundle {
        storeArchive {
            enable = true
        }
    }

    packaging {
        resources {
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/io.netty.versions.properties")
        }
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    implementation(project(":sample:core:analytics"))
    implementation(project(":sample:core:datastore"))
    implementation(project(":sample:integration:ads"))
    implementation(project(":sample:feature:apps"))
    implementation(project(":sample:feature:components"))
    implementation(project(":sample:feature:faq"))
    implementation(project(":sample:feature:onboarding"))
    implementation(project(":sample:feature:settings"))
    implementation(project(":sample:feature:startup"))
    implementation(project(":sample:feature:display"))
    implementation(project(":sample:feature:about"))
    implementation(project(":sample:feature:tiles"))
    implementation(project(":sample:widget"))
    implementation(project(":sample:core:apptoolkit"))
    implementation(project(":library:apptoolkit"))
    implementation(project(":library:core:common"))
    implementation(project(":library:core:ui"))
    implementation(project(":library:core:designsystem"))
    implementation(project(":library:navigation"))
    implementation(project(":library:feature:about"))
    implementation(project(":library:feature:faq"))
    implementation(project(":library:feature:issuereporter"))
    implementation(project(":library:feature:onboarding"))
    implementation(project(":library:feature:startup"))
    implementation(project(":library:feature:permissions"))
    implementation(project(":library:feature:settings"))
    implementation(project(":library:feature:support"))
    implementation(project(":library:integration:ads"))
    implementation(project(":library:integration:billing"))
    implementation(project(":library:integration:consent"))
    implementation(project(":library:integration:firebase"))
    implementation(project(":library:integration:review"))
    implementation(project(":library:integration:update"))

    implementation(libs.cronet.fallback)

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}
