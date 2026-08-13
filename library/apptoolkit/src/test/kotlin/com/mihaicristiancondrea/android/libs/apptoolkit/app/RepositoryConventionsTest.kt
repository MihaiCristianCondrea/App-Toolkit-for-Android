/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/** Guards the repositories placement and naming rules used by every active Android module. */
class RepositoryConventionsTest {

    @Test
    fun `repositories live in data repository packages`() {
        val misplaced = productionSources()
            .filter { it.name.endsWith(REPOSITORY_FILE_SUFFIX) }
            .filterNot { source -> DATA_REPOSITORY_PACKAGES.any(source.parentPath()::endsWith) }
            .map { it.relativePath() }

        assertThat(misplaced).isEmpty()
    }

    @Test
    fun `repository implementations do not use Impl naming`() {
        val legacyNames = productionSources()
            .filter { source ->
                source.name.contains(REPOSITORY_IMPL) ||
                    REPOSITORY_IMPL_DECLARATION.containsMatchIn(source.readText())
            }
            .map { it.relativePath() }

        assertThat(legacyNames).isEmpty()
    }

    @Test
    fun `default repositories correspond to a local repository contract`() {
        val repositoryFiles = productionSources()
            .filter { source -> DATA_REPOSITORY_PACKAGES.any(source.parentPath()::endsWith) }

        val contracts = repositoryFiles
            .filter { it.readText().contains(INTERFACE_DECLARATION) }
            .map { it.nameWithoutExtension }
            .toSet()

        val orphanedDefaults = repositoryFiles
            .filter { it.name.startsWith(DEFAULT_PREFIX) && it.name.endsWith(REPOSITORY_FILE_SUFFIX) }
            .filterNot { it.nameWithoutExtension.removePrefix(DEFAULT_PREFIX) in contracts }
            .map { it.relativePath() }

        assertThat(orphanedDefaults).isEmpty()
    }

    private fun productionSources(): List<File> = ACTIVE_SOURCE_ROOTS
        .flatMap { directory -> File(repositoryRoot, directory).walkTopDown().toList() }
        .filter { it.isFile && it.extension == KOTLIN_EXTENSION }
        .filter { MAIN_SOURCE_SET in it.invariantPath() }

    private fun File.parentPath(): String = parentFile?.invariantPath().orEmpty()

    private fun File.invariantPath(): String = path.replace(File.separatorChar, '/')

    private fun File.relativePath(): String = relativeTo(repositoryRoot).invariantPath()

    private companion object {
        val ACTIVE_SOURCE_ROOTS: List<String> = listOf("library", "sample")
        const val MAIN_SOURCE_SET = "/src/main/"
        const val KOTLIN_EXTENSION = "kt"
        // Both spellings are accepted while the rename is half-done. `data/repositories` is the
        // target named by the android-project-tree skill and is what `:sample` now uses; the
        // library still uses the singular because its packages are published API and renaming them
        // breaks every host's imports. Drop the singular entry once the library follows.
        val DATA_REPOSITORY_PACKAGES = listOf("/data/repositories", "/data/repositories")
        const val REPOSITORY_FILE_SUFFIX = "Repository.kt"
        const val REPOSITORY_IMPL = "RepositoryImpl"

        // A regex, not the literal "class RepositoryImpl": that substring never appears in a real
        // declaration, because the class name always carries a prefix — `class AboutRepositoryImpl`
        // does not contain it. The check silently passed everything, leaving the file-name check as
        // the only live rule and a mis-named class inside a correctly named file undetected.
        val REPOSITORY_IMPL_DECLARATION = Regex("""class\s+\w*RepositoryImpl\b""")
        const val INTERFACE_DECLARATION = "interface "
        const val DEFAULT_PREFIX = "Default"
        const val SETTINGS_FILE = "settings.gradle.kts"

        val repositoryRoot: File = generateSequence(File("").absoluteFile) { it.parentFile }
            .firstOrNull { File(it, SETTINGS_FILE).isFile }
            ?: error("Could not locate $SETTINGS_FILE above ${File("").absolutePath}")
    }
}
