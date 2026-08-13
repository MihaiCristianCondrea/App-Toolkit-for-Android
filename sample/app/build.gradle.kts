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

val hasGoogleServicesConfig: Boolean = listOf(
    "google-services.json",
    "src/debug/google-services.json",
    "src/release/google-services.json",
).any { path -> file(path).exists() }

if (hasGoogleServicesConfig) {
    apply(plugin = libs.plugins.google.mobile.services.get().pluginId)
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
    apply(plugin = libs.plugins.firebase.performance.get().pluginId)
}

val versioning = extensions.getByType<VersioningExtension>()
val appVersion = versioning.phoneVersion()

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit"
    compileSdk = appVersion.compileSdk

    defaultConfig {
        applicationId = "com.mihaicristiancondrea.android.apps"
        applicationIdSuffix = ".apptoolkit"
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
        buildConfigField("int", "APPS_LIST_AD_FREQUENCY", "4")
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

    buildTypes {
        release {
            val signingFile = rootProject.file("signing.properties")
            signingConfig = if (signingFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                null
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasGoogleServicesConfig) {
                configure<CrashlyticsExtension> {
                    mappingFileUploadEnabled = true
                }
            }
        }
    }


    buildFeatures {
        buildConfig = true
        compose = true
    }

    @Suppress("UnresolvedReference")
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xannotation-default-target=param-property")
        }
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
    implementation(project(":sample:core:common"))
    implementation(project(":library:apptoolkit"))
    implementation(project(":library:core:common"))
    implementation(project(":library:core:ui"))
    implementation(project(":library:core:designsystem"))
    implementation(project(":library:navigation"))
    implementation(project(":library:feature:about"))
    implementation(project(":library:feature:help"))
    implementation(project(":library:feature:issuereporter"))
    implementation(project(":library:feature:onboarding"))
    implementation(project(":library:feature:permissions"))
    implementation(project(":library:feature:settings"))
    implementation(project(":library:feature:support"))
    implementation(project(":library:integration:ads"))
    implementation(project(":library:integration:billing"))
    implementation(project(":library:integration:consent"))
    implementation(project(":library:integration:firebase"))
    implementation(project(":library:integration:review"))
    implementation(project(":library:integration:update"))

    implementation("org.chromium.net:cronet-fallback:143.7445.0")

    // Unit Tests

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}
