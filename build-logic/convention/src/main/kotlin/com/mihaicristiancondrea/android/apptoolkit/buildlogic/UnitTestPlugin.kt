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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

/**
 * Wires unit testing for an Android module: the JUnit 5 platform and the shared test dependency
 * bundles.
 *
 * Change rationale: every module used to repeat the same four blocks, the `mannodermaus` plugin,
 * `useJUnitPlatform()`, the JVM agent flag, and two dependency bundles. When the toolkit was split
 * into modules that repetition was simply not carried over, so 46 test files ended up in modules
 * with no test dependencies at all. `testDebugUnitTest` never ran anywhere and nothing reported it,
 * because a module with unresolvable test sources still assembles.
 *
 * Declaring it once means a new module cannot silently arrive without a working test source set:
 * applying `com.mihaicristiancondrea.android.apptoolkit.unit-test` is the whole contract.
 */
class UnitTestPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("de.mannodermaus.android-junit5")

        // Configured on the task rather than through `android.testOptions.unitTests`, which is what
        // that DSL does anyway: the Android extension does not exist until its own plugin has
        // finished applying, and a `plugins {}` block applies in source order. Going through the
        // tasks keeps this plugin usable wherever it is listed.
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            // mockk attaches its agent dynamically; the JVM warns without this from 21 on.
            jvmArgs("-XX:+EnableDynamicAgentLoading")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add("testImplementation", libs.findBundle("unitTest").get())
            add("testRuntimeOnly", libs.findBundle("unitTestRuntime").get())
        }
    }
}
