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

import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

/**
 * Enforces dependency and source-ownership rules for the sample application and the library.
 *
 * Applied to the root project, it registers [CHECK_TASK_NAME] and enforces the library's
 * dependency rules on every `:library:*` module. Sample modules apply it themselves through the
 * sample convention plugin.
 */
class ModuleBoundariesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target == target.rootProject) {
            registerRepositoryCheck(target)
            target.subprojects {
                if (path.startsWith(":library:")) enforceLibraryDependencies(this)
            }
            return
        }

        enforceProjectDependencies(target)
        target.tasks.matching { it.name == "check" }.configureEach {
            dependsOn(target.rootProject.tasks.named(CHECK_TASK_NAME))
        }
    }

    private fun registerRepositoryCheck(rootProject: Project) {
        rootProject.tasks.register(CHECK_TASK_NAME) {
            group = "verification"
            description = "Checks sample and library source ownership, package separation, and telemetry conventions."

            doLast {
                val sampleRoot = rootProject.layout.projectDirectory.dir("sample").asFile
                val sourceFiles = rootProject.fileTree(sampleRoot) {
                    include("**/src/**/*.kt")
                    exclude("**/build/**")
                }.files
                val libraryRoot = rootProject.layout.projectDirectory.dir("library").asFile
                val librarySourceFiles = rootProject.fileTree(libraryRoot) {
                    include("**/src/**/*.kt")
                    exclude("**/build/**")
                }.files

                val violations = mutableListOf<String>()
                checkSplitPackages(sampleRoot, sourceFiles, violations)
                checkSplitPackages(libraryRoot, librarySourceFiles, violations)
                checkAppOwnership(sampleRoot, sourceFiles, violations)
                checkCoreNavigation(sampleRoot, sourceFiles, violations)
                checkScreenTracking(sampleRoot, sourceFiles, violations)

                if (violations.isNotEmpty()) {
                    throw GradleException(
                        buildString {
                            appendLine("Module boundary violations:")
                            violations.sorted().forEach { appendLine("- $it") }
                        },
                    )
                }
            }
        }
    }

    private fun enforceProjectDependencies(target: Project) {
        target.afterEvaluate {
            val projectPath = target.path
            val projectDependencies = target.configurations
                .flatMap { configuration -> configuration.dependencies.withType(ProjectDependency::class.java) }
                .mapTo(mutableSetOf()) { dependency -> dependency.path }

            if (projectPath.startsWith(":sample:core:") || projectPath.startsWith(":sample:integration:")) {
                projectDependencies.forEach { dependencyPath ->
                    check(!dependencyPath.startsWith(":sample:feature:")) {
                        "Architecture violation: $projectPath cannot depend on feature module $dependencyPath"
                    }
                    check(dependencyPath != ":sample:app") {
                        "Architecture violation: $projectPath cannot depend on the app module"
                    }
                }
            }

            if (projectPath.startsWith(":sample:feature:")) {
                projectDependencies.forEach { dependencyPath ->
                    check(!dependencyPath.startsWith(":sample:feature:") || dependencyPath == projectPath) {
                        "Architecture violation: $projectPath cannot depend on sibling feature $dependencyPath"
                    }
                    check(dependencyPath != ":sample:app") {
                        "Architecture violation: $projectPath cannot depend on the app module"
                    }
                }
            }
        }
    }

    /**
     * The library's rules: shared modules never reach up into features or the assembly module, the
     * library never depends on the sample, and features do not depend on each other. The features
     * that still do are listed in [ALLOWED_LIBRARY_FEATURE_EDGES]; each edge is removed from the
     * list in the change that removes it from the build, so no new one can appear unnoticed.
     */
    private fun enforceLibraryDependencies(target: Project) {
        target.afterEvaluate {
            val projectPath = target.path
            val projectDependencies = target.configurations
                .flatMap { configuration -> configuration.dependencies.withType(ProjectDependency::class.java) }
                .mapTo(mutableSetOf()) { dependency -> dependency.path }
            val shared = projectPath.startsWith(":library:core:") ||
                projectPath.startsWith(":library:integration:") ||
                projectPath == ":library:navigation"

            projectDependencies.forEach { dependencyPath ->
                check(!dependencyPath.startsWith(":sample:")) {
                    "Architecture violation: library module $projectPath cannot depend on sample module $dependencyPath"
                }
                if (shared) {
                    check(!dependencyPath.startsWith(":library:feature:") && dependencyPath != ":library:apptoolkit") {
                        "Architecture violation: $projectPath cannot depend on $dependencyPath"
                    }
                }
                if (projectPath.startsWith(":library:feature:") && dependencyPath.startsWith(":library:feature:")) {
                    check(dependencyPath == projectPath || (projectPath to dependencyPath) in ALLOWED_LIBRARY_FEATURE_EDGES) {
                        "Architecture violation: $projectPath cannot depend on sibling feature $dependencyPath"
                    }
                }
            }
        }
        target.tasks.matching { it.name == "check" }.configureEach {
            dependsOn(target.rootProject.tasks.named(CHECK_TASK_NAME))
        }
    }

    private fun checkSplitPackages(
        root: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        val packageModules = mutableMapOf<String, MutableSet<String>>()
        sourceFiles.forEach { file ->
            val packageName = PACKAGE_REGEX.find(file.readText())?.groupValues?.get(1) ?: return@forEach
            packageModules.getOrPut(packageName, ::mutableSetOf).add(modulePath(root, file))
        }
        packageModules.filterValues { it.size > 1 }.forEach { (packageName, modules) ->
            violations += "Package $packageName is split across ${modules.sorted().joinToString()}"
        }
    }

    private fun checkAppOwnership(
        sampleRoot: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        sourceFiles.filterNot { modulePath(sampleRoot, it) == ":sample:app" }.forEach { file ->
            if (APP_OWNED_IMPORT_REGEX.containsMatchIn(file.readText())) {
                violations += "${file.relativeTo(sampleRoot)} imports app-owned composition code"
            }
        }
    }

    private fun checkCoreNavigation(
        sampleRoot: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        sourceFiles.filter { modulePath(sampleRoot, it) == ":sample:core:navigation" }.forEach { file ->
            if (FEATURE_IMPORT_REGEX.containsMatchIn(file.readText())) {
                violations += "${file.relativeTo(sampleRoot)} imports a product feature"
            }
        }
    }

    private fun checkScreenTracking(
        sampleRoot: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        sourceFiles.forEach { file ->
            if (INLINE_SCREEN_NAME_REGEX.containsMatchIn(file.readText())) {
                violations += "${file.relativeTo(sampleRoot)} uses an inline analytics screen name"
            }
        }
    }

    /** The Gradle path of the module under [root], `sample` or `library`, that owns [file]. */
    private fun modulePath(root: File, file: File): String {
        val segments = file.relativeTo(root).invariantSeparatorsPath.split('/')
        val moduleSegments = if (segments.first() in NESTED_MODULE_GROUPS) {
            segments.take(2)
        } else {
            segments.take(1)
        }
        return ":${root.name}:${moduleSegments.joinToString(":")}"
    }

    private companion object {
        const val CHECK_TASK_NAME = "checkModuleBoundaries"
        val NESTED_MODULE_GROUPS = setOf("core", "feature", "integration")

        /**
         * Library feature-to-feature dependencies that exist today. Each goes when its feature
         * registers its pages in the shell graph and opens the other by key instead.
         */
        val ALLOWED_LIBRARY_FEATURE_EDGES = setOf(
            ":library:feature:about" to ":library:feature:licenses",
            ":library:feature:advanced" to ":library:feature:issuereporter",
            ":library:feature:faq" to ":library:feature:licenses",
            ":library:feature:onboarding" to ":library:feature:settings",
            ":library:feature:permissions" to ":library:feature:settings",
            ":library:feature:settings" to ":library:feature:about",
            ":library:feature:settings" to ":library:feature:advanced",
            ":library:feature:settings" to ":library:feature:diagnostics",
            ":library:feature:settings" to ":library:feature:display",
            ":library:feature:settings" to ":library:feature:faq",
            ":library:feature:settings" to ":library:feature:issuereporter",
            ":library:feature:settings" to ":library:feature:privacy",
            ":library:feature:settings" to ":library:feature:theme",
        )
        val PACKAGE_REGEX = Regex("(?m)^package\\s+([A-Za-z0-9_.]+)")
        val APP_OWNED_IMPORT_REGEX = Regex(
            "(?m)^import\\s+com\\.mihaicristiancondrea\\.android\\.apps\\.apptoolkit\\.app\\.(main|integration|navigation)(\\.|$)",
        )
        val FEATURE_IMPORT_REGEX = Regex(
            "(?m)^import\\s+com\\.mihaicristiancondrea\\.android\\.apps\\.apptoolkit\\.feature\\.(apps|components|onboarding|settings|tiles)(\\.|$)",
        )
        val INLINE_SCREEN_NAME_REGEX = Regex("screenName\\s*=\\s*\"")
    }
}
