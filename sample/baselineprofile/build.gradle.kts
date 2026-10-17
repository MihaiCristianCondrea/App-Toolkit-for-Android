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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.androidx.baselineprofile)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit.baselineprofile"
    compileSdk = versioning.compileSdk
    compileSdkMinor = versioning.compileSdkMinor
    targetProjectPath = ":sample:app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    defaultConfig {
        minSdk = maxOf(28, versioning.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_21
}

baselineProfile {
    useConnectedDevices = true
}

androidComponents {
    onVariants { variant ->
        val testPackage = "com.mihaicristiancondrea.android.apps.apptoolkit.baselineprofile"
        val classes = when (variant.buildType) {
            "nonMinifiedRelease" -> "$testPackage.BaselineProfileGenerator"
            "benchmarkRelease" -> "$testPackage.StartupBenchmarks,$testPackage.ScrollingBenchmarks"
            else -> null
        }
        if (classes != null) variant.instrumentationRunnerArguments.put("class", classes)
    }
}

dependencies {
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.uiautomator)
}
