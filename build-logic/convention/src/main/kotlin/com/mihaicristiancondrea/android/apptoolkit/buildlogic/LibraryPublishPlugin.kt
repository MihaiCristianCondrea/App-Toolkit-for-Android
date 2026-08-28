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
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

/**
 * Publishes a library module so JitPack serves it.
 *
 * Why every module needs this: `:library:apptoolkit` re-exports the whole toolkit with
 * `api(project(...))`, so its generated POM lists nineteen sibling modules as dependencies. Only
 * `apptoolkit` and `navigation` used to apply `maven-publish`, so the other eighteen coordinates in
 * that POM pointed at artifacts that were never published. A host adding the library resolved the
 * POM and then failed on every one of them.
 *
 * The artifact id is the Gradle project name, which is exactly what the consuming POM already
 * references, `:library:core:ui` is published as `ui`, `:library:feature:about` as `about`. Group
 * and version come from the root `subprojects` block, so a module cannot drift from the release it
 * ships in.
 */
class LibraryPublishPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("maven-publish")

        extensions.getByType<LibraryExtension>().publishing {
            singleVariant(PUBLISHED_VARIANT) {
                withSourcesJar()
            }
        }

        extensions.getByType<PublishingExtension>().publications.create<MavenPublication>(
            PUBLISHED_VARIANT
        ) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            // The release component only exists once the Android plugin has finished creating its
            // variants, which happens after this block runs.
            afterEvaluate {
                from(components.getByName(PUBLISHED_VARIANT))
            }
        }
    }

    private companion object {
        const val PUBLISHED_VARIANT = "release"
    }
}
