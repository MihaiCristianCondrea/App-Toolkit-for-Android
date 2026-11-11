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

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

class LibraryDocsPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.dokka")

        dependencies.add("dokkaPlugin", DOKKA_SITE_PLUGIN)
        checkDokkaWarnings()

        val moduleDoc = tasks.register<ReadmeModuleDocTask>("generateDokkaModuleDoc") {
            readme.set(layout.projectDirectory.file("README.md"))
            moduleName.set(target.path)
            output.set(layout.buildDirectory.file("generated/dokka/Module.md"))
        }
        val android = extensions.getByName("androidComponents") as AndroidComponentsExtension<*, *, *>
        android.onVariants(android.selector().withBuildType(PUBLISHED_VARIANT)) { variant ->
            val runtimeClasses = listOf("android-classes-jar", ArtifactTypeDefinition.JAR_TYPE).map { type ->
                variant.runtimeConfiguration.incoming.artifactView {
                    attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, type)
                    // The other view handles artifacts that cannot be converted to this type.
                    isLenient = true
                }.files
            }
            extensions.configure<DokkaExtension> {
                dokkaSourceSets.configureEach {
                    if (name == PUBLISHED_VARIANT) classpath.from(runtimeClasses)
                }
            }
        }

        val modulePath = projectDir.relativeTo(rootDir).invariantSeparatorsPath
        val sources = projectDir.resolve("src")
        extensions.configure<DokkaExtension> {
            moduleName.set(path)
            dokkaSourceSets.configureEach {
                // Android variants share src/main. Document the variant shipped to consumers.
                suppress.set(name != PUBLISHED_VARIANT)
                if (name == PUBLISHED_VARIANT) {
                    includes.from(moduleDoc.flatMap { it.output })
                }
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

const val DOKKA_SITE_PLUGIN = "com.mihaicristiancondrea.android.apptoolkit.dokka:site:1.0"
