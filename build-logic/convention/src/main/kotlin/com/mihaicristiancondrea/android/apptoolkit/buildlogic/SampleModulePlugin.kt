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
 * Baseline configuration shared by every `:sample:*` library module.
 *
 * The host app was one module, so its Android/Compose/versioning setup was written once. Splitting
 * it into ten would have copied that block ten times, and the modules would drift the way the
 * library modules did before the JVM-target and unit-test plugins landed — silently, because a
 * module with the wrong `compileSdk` or a missing `compose = true` still assembles until something
 * in it needs the missing piece.
 *
 * `buildConfig` is on for all of them: several sample modules branch on `BuildConfig.DEBUG`, which
 * is per-module but reflects the same build type, so each needs its own generated class. Fields
 * that are genuinely app-wide — the application id, version, GitHub token — stay in `:sample:app`,
 * which is the only module that can meaningfully declare them.
 */
class SampleModulePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        // No `org.jetbrains.kotlin.android` here: AGP 9 compiles Kotlin itself, and applying the
        // standalone plugin on top of the built-in support fails with an empty Throwable. Every
        // library module in this repo relies on the same built-in path.
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.versioning")
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.unit-test")

        val versioning = extensions.getByType<VersioningExtension>()
        extensions.getByType<LibraryExtension>().apply {
            compileSdk = versioning.compileSdk
            defaultConfig.minSdk = versioning.minSdk
            buildFeatures.compose = true
            buildFeatures.buildConfig = true
        }

        // Applied last, and through the plugin manager rather than the `plugins {}` block, because
        // it reads the Android extension configured just above.
        pluginManager.apply("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    }
}
