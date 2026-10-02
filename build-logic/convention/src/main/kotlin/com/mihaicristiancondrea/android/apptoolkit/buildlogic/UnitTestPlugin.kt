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
 * Configures Android unit tests with JUnit 5, shared test bundles, and dynamic agent loading
 * for MockK.
 */
class UnitTestPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("de.mannodermaus.android-junit5")

        // Configure Test tasks directly so this plugin does not depend on Android-extension creation order.
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
