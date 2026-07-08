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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.d4rk.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.compose)
    alias(notation = libs.plugins.kotlin.parcelize)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.about.libraries)
    alias(notation = libs.plugins.mannodermaus.android.junit5)
    id("com.d4rk.android.apptoolkit.versioning")
}

val versioning = extensions.getByType<VersioningExtension>()

android {

    namespace = "com.d4rk.android.libs.apptoolkit"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }

}

dependencies {
    // Internal modules
    api(project(":core"))
    api(project(":navigation"))
    api(project(":playservices"))

    // AndroidX (Core / platform)
    api(dependencyNotation = libs.bundles.androidx.core)
    api(dependencyNotation = libs.androidx.window)
    api(dependencyNotation = libs.androidx.constraintlayout.compose)
    api(dependencyNotation = libs.bundles.androidx.glance)


    // Compose
    api(dependencyNotation = platform(libs.androidx.compose.bom))
    api(dependencyNotation = libs.bundles.androidx.compose)

    // Navigation3 + Lifecycle
    api(dependencyNotation = libs.bundles.androidx.navigation3)
    api(dependencyNotation = libs.bundles.androidx.lifecycle)

    // Firebase
    api(dependencyNotation = platform(libs.firebase.bom))
    api(dependencyNotation = libs.bundles.firebase)

    // Google (Material + UMP + Ads) + Google Play (Billing/Review/Update/Integrity)
    api(dependencyNotation = libs.bundles.google)
    api(dependencyNotation = libs.bundles.google.play)

    // Image loading
    api(dependencyNotation = libs.bundles.coil)

    // Kotlin Coroutines & Serialization
    api(dependencyNotation = libs.bundles.kotlinx)

    // Networking (Ktor)
    api(dependencyNotation = platform(libs.ktor.bom))
    api(dependencyNotation = libs.bundles.ktor)

    // Dependency Injection
    api(dependencyNotation = libs.bundles.koin)

    // UI utilities
    api(dependencyNotation = libs.bundles.ui.effects)
    api(dependencyNotation = libs.bundles.ui.richtext)

    // Unit Tests
    testImplementation(dependencyNotation = libs.bundles.unitTest)
    testRuntimeOnly(dependencyNotation = libs.bundles.unitTestRuntime)

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}
