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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Aligns Java and Kotlin bytecode targets across modules, including test dependencies with
 * inline functions. Apply after the Android plugin. Java targets use `android.compileOptions`
 * because AGP compares those values with Kotlin's target.
 */
class JvmTargetPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions.jvmTarget.set(JVM_TARGET)
        }

        when (val android = extensions.findByName("android")) {
            is ApplicationExtension -> android.compileOptions {
                sourceCompatibility = JAVA_VERSION
                targetCompatibility = JAVA_VERSION
            }

            is LibraryExtension -> android.compileOptions {
                sourceCompatibility = JAVA_VERSION
                targetCompatibility = JAVA_VERSION
            }

            else -> error(
                "Apply com.mihaicristiancondrea.android.apptoolkit.jvm-target after the Android " +
                        "application or library plugin in $path."
            )
        }
    }

    private companion object {
        val JAVA_VERSION: JavaVersion = JavaVersion.VERSION_21
        val JVM_TARGET: JvmTarget = JvmTarget.JVM_21
    }
}
