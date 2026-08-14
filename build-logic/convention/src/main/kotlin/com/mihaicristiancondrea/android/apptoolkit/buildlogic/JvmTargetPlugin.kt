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
 * Pins Java and Kotlin bytecode to the same JVM target across every module.
 *
 * Change rationale: only two modules declared a target after the toolkit was split up, so the other
 * twenty silently compiled to Java 11. Production code got away with it; test code did not, because
 * inlining a Java 17 `inline fun` from mockk or `kotlinx-coroutines-test` into an 11-target module
 * is a hard compile error. The fix belongs in one place — a module should not have to remember.
 *
 * Apply this *after* the Android application/library plugin in the `plugins {}` block: a plugins
 * block applies in source order, and the Android extension does not exist until its own plugin has
 * finished applying. Java has to go through `android.compileOptions` rather than the `JavaCompile`
 * tasks, because that DSL value is what AGP compares against Kotlin's when it checks the two agree.
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
                        "application or library plugin in ${'$'}path." // FIXME: An interpolation prefix can simplify the string
            )
        }
    }

    private companion object {
        val JAVA_VERSION: JavaVersion = JavaVersion.VERSION_21
        val JVM_TARGET: JvmTarget = JvmTarget.JVM_21
    }
}
