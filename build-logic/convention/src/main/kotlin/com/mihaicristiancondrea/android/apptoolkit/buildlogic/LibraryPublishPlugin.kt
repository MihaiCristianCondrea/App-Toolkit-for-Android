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
 * Publishes each library dependency re-exported by `:library:apptoolkit` so its POM refers to
 * available artifacts. Artifact IDs use project names; group and version come from the root
 * project configuration.
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
