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
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    id("com.mihaicristiancondrea.android.apptoolkit.library-publish")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.core.common"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.instrumentationTest)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)

    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.play.services)
    api(libs.koin.android)
    api(libs.androidx.multidex)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.process)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.appcheck.playintegrity)
    api(libs.firebase.crashlytics)
    api(libs.firebase.perf)

    api(libs.google.ads.mobile.sdk)

    // api(project(":library:core:datastore")) // Removed to break circular dependency
}
