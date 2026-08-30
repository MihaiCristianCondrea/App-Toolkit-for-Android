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
import org.gradle.api.artifacts.ProjectDependency

/**
 * Enforces architectural boundaries for the sample modules.
 * - core modules cannot depend on feature modules.
 * - feature modules cannot depend on sibling feature modules.
 * - core/feature modules cannot depend on the app module.
 */
class ModuleBoundariesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            val projectPath = target.path
            
            val projectDependencies = mutableSetOf<String>()
            target.configurations.forEach { config ->
                config.dependencies.forEach { dep ->
                    if (dep is ProjectDependency) {
                        projectDependencies.add(dep.path)
                    }
                }
            }

            if (projectPath.startsWith(":sample:core:")) {
                projectDependencies.forEach { dep ->
                    if (dep.startsWith(":sample:feature:")) {
                        throw IllegalStateException("Architecture violation: Core module $projectPath cannot depend on feature module $dep")
                    }
                    if (dep == ":sample:app") {
                        throw IllegalStateException("Architecture violation: Core module $projectPath cannot depend on app module")
                    }
                }
            }

            if (projectPath.startsWith(":sample:feature:")) {
                projectDependencies.forEach { dep ->
                    if (dep.startsWith(":sample:feature:") && dep != projectPath) {
                        throw IllegalStateException("Architecture violation: Feature module $projectPath cannot depend on sibling feature module $dep")
                    }
                    if (dep == ":sample:app") {
                        throw IllegalStateException("Architecture violation: Feature module $projectPath cannot depend on app module")
                    }
                }
            }
        }
    }
}
