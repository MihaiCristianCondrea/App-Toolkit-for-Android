/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apptoolkit.buildlogic

import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

/** Enforces dependency and source-ownership rules for the sample application. */
class ModuleBoundariesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target == target.rootProject) {
            registerRepositoryCheck(target)
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
            description = "Checks sample source ownership, package separation, and telemetry conventions."

            doLast {
                val sampleRoot = rootProject.layout.projectDirectory.dir("sample").asFile
                val sourceFiles = rootProject.fileTree(sampleRoot) {
                    include("**/src/**/*.kt")
                    exclude("**/build/**")
                }.files

                val violations = mutableListOf<String>()
                checkSplitPackages(sampleRoot, sourceFiles, violations)
                checkAppOwnership(sampleRoot, sourceFiles, violations)
                checkCoreNavigation(sampleRoot, sourceFiles, violations)
                checkScreenTracking(sampleRoot, sourceFiles, violations)

                if (violations.isNotEmpty()) {
                    throw GradleException(
                        buildString {
                            appendLine("Sample module boundary violations:")
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

    private fun checkSplitPackages(
        sampleRoot: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        val packageModules = mutableMapOf<String, MutableSet<String>>()
        sourceFiles.forEach { file ->
            val packageName = PACKAGE_REGEX.find(file.readText())?.groupValues?.get(1) ?: return@forEach
            packageModules.getOrPut(packageName, ::mutableSetOf).add(modulePath(sampleRoot, file))
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

    private fun modulePath(sampleRoot: File, file: File): String {
        val segments = file.relativeTo(sampleRoot).invariantSeparatorsPath.split('/')
        val moduleSegments = if (segments.first() in NESTED_MODULE_GROUPS) {
            segments.take(2)
        } else {
            segments.take(1)
        }
        return ":sample:${moduleSegments.joinToString(":")}"
    }

    private companion object {
        const val CHECK_TASK_NAME = "checkModuleBoundaries"
        val NESTED_MODULE_GROUPS = setOf("core", "feature", "integration")
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
