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

plugins {
    id("com.mihaicristiancondrea.android.apptoolkit.sample-module")
    // Routes are @Serializable: the shell saves its back stacks through kotlinx.serialization.
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles"

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all { test ->
            test.jvmArgs(
                "--add-opens=java.base/java.io=ALL-UNNAMED",
                "--add-opens=java.base/java.lang=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED",
                "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
            )
        }
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.test.ext.junit)
    testRuntimeOnly(libs.test.junit.vintage.engine)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(project(":sample:core:analytics"))
    api(project(":sample:integration:ads"))
    api(project(":library:apptoolkit"))
}
