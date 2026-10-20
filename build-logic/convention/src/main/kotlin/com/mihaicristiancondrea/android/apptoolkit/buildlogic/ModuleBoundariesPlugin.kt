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
            description = "Checks sample and library source ownership, package separation, tree conventions, analytics ownership, and telemetry conventions."

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
                checkSampleCore(sampleRoot, sourceFiles, violations)
                checkScreenTracking(sampleRoot, sourceFiles, violations)
                checkTreeConventions(sampleRoot, sourceFiles, violations)
                checkTreeConventions(libraryRoot, librarySourceFiles, violations)
                checkAnalyticsOwnership(sampleRoot, sourceFiles, violations)
                checkAnalyticsOwnership(libraryRoot, librarySourceFiles, violations)

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
     * library never depends on the sample, and features do not depend on each other: a feature
     * opens another's page by key, and `:library:apptoolkit` is the one module that knows them all.
     */
    private fun enforceLibraryDependencies(target: Project) {
        target.afterEvaluate {
            val projectPath = target.path
            val projectDependencies = target.configurations
                .flatMap { configuration -> configuration.dependencies.withType(ProjectDependency::class.java) }
                .mapTo(mutableSetOf()) { dependency -> dependency.path }
            val shared = projectPath.startsWith(":library:core:") ||
                projectPath.startsWith(":library:integration:") ||
                projectPath == ":library:navigation" ||
                projectPath == ":library:shell"

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
                    check(dependencyPath == projectPath) {
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

    /**
     * The sample's core modules sit under its features, so none may import one. The features are
     * read from `sample/feature/`, so a new feature module is covered without editing this rule.
     */
    private fun checkSampleCore(
        sampleRoot: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        val features = sampleRoot.resolve("feature").listFiles(File::isDirectory).orEmpty().map { it.name }
        if (features.isEmpty()) return
        val featureImport = Regex(
            "(?m)^import\\s+com\\.mihaicristiancondrea\\.android\\.apps\\.apptoolkit\\.feature\\." +
                "(${features.joinToString("|") { Regex.escape(it) }})(\\.|$)",
        )
        sourceFiles.filter { modulePath(sampleRoot, it).startsWith(":sample:core:") }.forEach { file ->
            if (featureImport.containsMatchIn(file.readText())) {
                violations += "${file.relativeTo(sampleRoot)} imports a product feature"
            }
        }
    }

    /**
     * The objective part of the `layered-tree-review` skill: package names, where the standard
     * file kinds live, and mapper names. Judgement calls, such as whether a mapper or a use case
     * is worth having, stay in the skill.
     */
    private fun checkTreeConventions(
        root: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        val mainFiles = sourceFiles.filter { "/src/main/" in it.invariantSeparatorsPath }
        val packagesByModule = mutableMapOf<String, MutableSet<String>>()
        mainFiles.forEach { file ->
            val text = file.readText()
            val packageName = PACKAGE_REGEX.find(text)?.groupValues?.get(1) ?: return@forEach
            val module = modulePath(root, file)
            packagesByModule.getOrPut(module, ::mutableSetOf).add(packageName)
            val where = "${file.relativeTo(root)}"
            val segments = packageName.split('.')

            segments.firstOrNull { it in BANNED_PACKAGE_SEGMENTS }?.let { segment ->
                violations += "$where is in a `$segment` package; ${BANNED_PACKAGE_SEGMENTS.getValue(segment)}"
            }
            FILE_PLACEMENT.forEach { (suffix, rule) ->
                if (file.name.endsWith(suffix) && !rule.allows(packageName)) {
                    violations += "$where: ${rule.message}"
                }
            }
            VAGUE_MAPPER_REGEX.find(text)?.let { match ->
                violations += "$where declares `${match.groupValues[1]}()`; name a mapper after the type it returns"
            }
        }

        packagesByModule.forEach { (module, packages) ->
            packages.mapNotNull { DOMAIN_ROOT_REGEX.find(it)?.value }.toSet().forEach { domainRoot ->
                if ("$domainRoot.usecases" !in packages) {
                    violations += "$module: `$domainRoot` holds no use cases; move its models to `data.models`"
                }
            }
        }
    }

    /**
     * Product analytics belong to the UI layer. Code under `data` and `domain` packages may take
     * `DiagnosticsReporter` for breadcrumbs and non-fatal reports, and `TelemetryCollection` where
     * it applies consent, but never the analytics contracts, events, GA4 helpers or SDK. A
     * repository returns its outcome and the screen that asked reports it.
     *
     * The telemetry transport is the deliberate exception: it implements analytics, so it has to
     * name them. [ANALYTICS_INFRASTRUCTURE_MODULES] lists it.
     */
    private fun checkAnalyticsOwnership(
        root: File,
        sourceFiles: Set<File>,
        violations: MutableList<String>,
    ) {
        sourceFiles.filter { "/src/main/" in it.invariantSeparatorsPath }.forEach { file ->
            if (modulePath(root, file) in ANALYTICS_INFRASTRUCTURE_MODULES) return@forEach
            val text = file.readText()
            val packageName = PACKAGE_REGEX.find(text)?.groupValues?.get(1) ?: return@forEach
            val layer = packageName.split('.').firstOrNull { it == "data" || it == "domain" } ?: return@forEach
            ANALYTICS_REFERENCE_REGEX.find(text)?.let { match ->
                violations += "${file.relativeTo(root)} reports analytics from the $layer layer (" +
                    "${match.value.substringAfterLast('.')}); return the outcome and report it from the UI, " +
                    "or use DiagnosticsReporter for a breadcrumb or failure"
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

        val PACKAGE_REGEX = Regex("(?m)^package\\s+([A-Za-z0-9_.]+)")
        val APP_OWNED_IMPORT_REGEX = Regex(
            "(?m)^import\\s+com\\.mihaicristiancondrea\\.android\\.apps\\.apptoolkit\\.app\\.(main|integration|navigation)(\\.|$)",
        )

        /** Package names outside the tree's vocabulary, with the name to use instead. */
        val BANNED_PACKAGE_SEGMENTS = mapOf(
            "presentation" to "UI code lives in `ui`",
            "datasource" to "data sources live directly in `data/local` or `data/remote`",
            "datasources" to "data sources live directly in `data/local` or `data/remote`",
            "usecase" to "use `domain.usecases`",
            "repository" to "use `data.repositories`",
            "mapper" to "use `mappers`",
            "model" to "use `models`",
            "state" to "use `states`",
            "view" to "use `views`",
            "contract" to "use `contracts`",
            "util" to "use `utils`",
            "helper" to "use `utils`",
            "helpers" to "use `utils`",
            "base" to "shared screen parts live in `core.ui.screen`",
        )

        class Placement(val message: String, val allows: (String) -> Boolean)

        /** Where the file kinds named by their suffix must live. */
        val FILE_PLACEMENT = mapOf(
            "Dto.kt" to Placement("a DTO lives in `data.remote.models`") { it.endsWith(".data.remote.models") },
            "UseCase.kt" to Placement("a use case lives in `domain.usecases`") { it.endsWith(".domain.usecases") },
            "UiState.kt" to Placement("a UI state lives in a `ui` package's `states`") {
                it.endsWith(".states") && ".ui" in it
            },
            "ViewModel.kt" to Placement("a ViewModel lives in a `ui` package, outside `views`") {
                ".ui" in it && ".views" !in it
            },
            // TelemetryRepository keeps its published name in the telemetry package: it is a
            // cross-cutting contract, not application data.
            "Repository.kt" to Placement("a repository lives in `data.repositories`") {
                it.endsWith(".data.repositories") || it.endsWith(".core.testing") ||
                    it.endsWith(".core.common.telemetry")
            },
        )

        val VAGUE_MAPPER_REGEX = Regex("(?m)^\\s*(?:\\w+\\s+)*fun\\s+(?:<[^>]+>\\s*)?[\\w.<>?, ]+\\.(toDomain|toModel|toData|toUi|toEntity|toDto)\\(")
        val DOMAIN_ROOT_REGEX = Regex("^.*?\\.domain(?=\\.|$)")
        /** Modules that implement telemetry, and so may name analytics from their data layer. */
        val ANALYTICS_INFRASTRUCTURE_MODULES = setOf(":library:integration:firebase")

        /** The analytics contracts, events, GA4 helpers, UI analytics and the analytics SDK. */
        val ANALYTICS_REFERENCE_REGEX = Regex(
            "com\\.mihaicristiancondrea\\.android\\.libs\\.apptoolkit\\.(" +
                "core\\.common\\.telemetry\\.(TelemetryRepository|AnalyticsReporter|models)\\b|" +
                "core\\.common\\.utils\\.extensions\\.analytics\\b|" +
                "core\\.ui\\.(views|models)\\.analytics\\b)" +
                "|com\\.google\\.firebase\\.analytics\\b",
        )
        val INLINE_SCREEN_NAME_REGEX = Regex("screenName\\s*=\\s*\"")
    }
}
