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

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    // The screenshot tests' keys are @Serializable, as every key on a shell back stack must be.
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.roborazzi)
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    id("com.mihaicristiancondrea.android.apptoolkit.library-publish")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.shell"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all { test ->
            // Robolectric reaches into these JDK internals; newer JDKs close them by default.
            test.jvmArgs(
                "--add-opens=java.base/java.io=ALL-UNNAMED",
                "--add-opens=java.base/java.lang=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED",
                "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
            )
        }
    }
}

// `./gradlew :library:shell:recordRoborazziDebug` rewrites the reference images;
// `verifyRoborazziDebug` fails when the chrome no longer matches them.
roborazzi {
    outputDir.set(file("src/test/screenshots"))
}

dependencies {
    testImplementation(project(":library:core:testing"))
    // The graph, navigator and scenes the shell hosts, and the page frame and buttons it draws with,
    // are part of what an app using the shell writes against.
    api(project(":library:navigation"))
    api(project(":library:core:ui"))

    // The shell's own settings store.
    implementation(libs.androidx.datastore.preferences)

    // Screenshot tests: Robolectric drives a real activity under JUnit 4, so this module, and only
    // this one, runs the JUnit 4 engine beside JUnit 5.
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.test.roborazzi)
    testImplementation(libs.test.roborazzi.compose)
    testRuntimeOnly(libs.test.junit.vintage.engine)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
