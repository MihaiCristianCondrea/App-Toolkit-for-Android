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
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

class LibraryDocsPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.dokka")

        val modulePath = projectDir.relativeTo(rootDir).invariantSeparatorsPath
        val sources = projectDir.resolve("src")
        extensions.configure<DokkaExtension> {
            moduleName.set(path)
            dokkaSourceSets.configureEach {
                // Android variants share src/main. Document the variant shipped to consumers.
                suppress.set(name != PUBLISHED_VARIANT)
                documentedVisibilities.set(setOf(VisibilityModifier.Public))
                reportUndocumented.set(false)
                sourceLink {
                    localDirectory.set(sources)
                    remoteUrl(
                        "https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/blob/develop/$modulePath/src"
                    )
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}
