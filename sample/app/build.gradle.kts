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

// The Play Store identity of an already-released app. Kept next to the Firebase check below
// because the two have to agree: `google-services.json` is matched by package name, so changing
// one without the other fails the build.
val releasedApplicationId = "com.d4rk.android.apps.apptoolkit"

val googleServicesFiles: List<File> = listOf(
    "google-services.json",
    "src/debug/google-services.json",
    "src/release/google-services.json",
).map(::file).filter(File::exists)

// Checking that a config file exists is not the same as checking it is the right one. The Google
// Services plugin matches by package name and fails with "No matching client found for package
// name", which reads as a build misconfiguration rather than what it is: this Firebase project has
// no app registered for the id we ship under. Detecting it here turns that into an actionable
// message, and lets a contributor without the real config still build.
// Matched with a regex rather than an exact substring: the file is generated JSON and its spacing
// is not a contract we control.
val packageNamePattern = Regex(
    """"package_name"\s*:\s*"${Regex.escape(releasedApplicationId)}""""
)
val hasMatchingGoogleServicesConfig: Boolean = googleServicesFiles.any { configFile ->
    packageNamePattern.containsMatchIn(configFile.readText())
}

// A config that is present but names a different package is the dangerous case: it looks
// configured, and the only symptom is that Firebase quietly does nothing.
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
                "in the Firebase console and re-download the file. See docs/application-id.md."
    )
}

// `google-services.json` is gitignored, so an absent file is the normal state on CI and on any
// clone, it means "not configured here", which is obvious and harmless. A file that is present but
// names a different package means "configured wrong", which is invisible: Firebase is skipped and
// the release ships with no crash reporting. Only the second case fails the build; failing on the
// first would break `./gradlew build` on CI, which assembles release variants.
gradle.taskGraph.whenReady {
    val assemblesRelease = allTasks.any { task ->
        task.project == project && task.name.contains("Release")
    }
    check(!hasMismatchedGoogleServicesConfig || !assemblesRelease) {
        "Refusing to build a release with a mismatched google-services.json: it has no client for " +
                "'$releasedApplicationId', so Crashlytics would be silently disabled. " +
                "See docs/application-id.md."
    }
}

val versioning = extensions.getByType<VersioningExtension>()
val appVersion = versioning.phoneVersion()

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit"
    compileSdk = appVersion.compileSdk

    defaultConfig {
        // The Play Store identity of an app that is already released. It must not follow the
        // `namespace` above: `namespace` names the generated R/BuildConfig classes and can be
        // renamed freely, while changing `applicationId` publishes a different app and strands
        // every existing install. See docs/application-id.md.
        applicationId = "com.d4rk.android.apps.apptoolkit"
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
    implementation(project(":sample:core:analytics"))
    implementation(project(":sample:core:common"))
    implementation(project(":sample:core:datastore"))
    implementation(project(":sample:core:navigation"))
    implementation(project(":sample:feature:ads"))
    implementation(project(":sample:feature:apps"))
    implementation(project(":sample:feature:components"))
    implementation(project(":sample:core:shell"))
    implementation(project(":sample:feature:onboarding"))
    implementation(project(":sample:feature:settings"))
    implementation(project(":sample:feature:tiles"))
    implementation(project(":sample:widget"))
    implementation(project(":sample:core:apptoolkit"))
    implementation(project(":sample:core:ui"))
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

    implementation(libs.cronet.fallback)

    // Unit Tests

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}
