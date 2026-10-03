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

package com.mihaicristiancondrea.android.apptoolkit.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Shared Android, Compose, versioning, and test configuration for sample library modules. Each
 * module generates its own build-type `BuildConfig`; app-wide identity and version fields stay
 * in `:sample:app`.
 */
class SampleModulePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        // Use AGP's built-in Kotlin support; applying the standalone Android Kotlin plugin conflicts with it.
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.versioning")
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.unit-test")
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.module-boundaries")

        val versioning = extensions.getByType<VersioningExtension>()
        extensions.getByType<LibraryExtension>().apply {
            compileSdk = versioning.compileSdk
            defaultConfig.minSdk = versioning.minSdk
            buildFeatures.compose = true
            buildFeatures.buildConfig = true
        }

        // Apply after configuring the Android extension that this plugin reads.
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    }
}
