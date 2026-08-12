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

import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

val publishingArtifactId = providers.gradleProperty("PUBLISHING_ARTIFACT_ID")
val jitpackGroupId = providers.gradleProperty("JITPACK_GROUP_ID")
val publishingVersion = providers.gradleProperty("PUBLISHING_VERSION")

plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.compose)
    alias(notation = libs.plugins.kotlin.parcelize)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.about.libraries)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    `maven-publish`
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
}

val versioning = extensions.getByType<VersioningExtension>()

android {

    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit"
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



    buildFeatures {
        compose = true
    }


    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

// `RepositoryConventionsTest` reads the library source tree rather than the classpath, and Gradle
// cannot infer that. Without declaring it, moving a repository into the wrong package leaves the
// test task up to date and the violation ships unnoticed — the exact failure the test exists to
// prevent.
tasks.withType<Test>().configureEach {
    // `build` is excluded so the scan stops before it reaches this module's own intermediates —
    // Gradle rejects an input that overlaps another task's output directory.
    inputs.files(
        rootProject.fileTree(rootProject.layout.projectDirectory.dir("library")) {
            include("**/src/main/**/*.kt")
            exclude("**/build/**")
        }
    )
        .withPropertyName("librarySources")
        .withPathSensitivity(PathSensitivity.RELATIVE)
}

dependencies {
    testImplementation(project(":library:core:testing"))
    // Internal modules
    api(project(":library:core:common"))
    api(project(":library:core:datastore"))
    api(project(":library:core:network"))
    api(project(":library:core:ui"))
    api(project(":library:core:designsystem"))
    api(project(":library:navigation"))
    api(project(":library:feature:about"))
    api(project(":library:feature:help"))
    api(project(":library:feature:issuereporter"))
    api(project(":library:feature:onboarding"))
    api(project(":library:feature:permissions"))
    api(project(":library:feature:settings"))
    api(project(":library:feature:support"))
    api(project(":library:integration:ads"))
    api(project(":library:integration:billing"))
    api(project(":library:integration:consent"))
    api(project(":library:integration:firebase"))
    api(project(":library:integration:review"))
    api(project(":library:integration:update"))

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

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = jitpackGroupId.get()
            artifactId = publishingArtifactId.get()
            version = publishingVersion.get()

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
