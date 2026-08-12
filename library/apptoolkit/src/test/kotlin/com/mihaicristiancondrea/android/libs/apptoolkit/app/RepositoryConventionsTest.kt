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

package com.mihaicristiancondrea.android.libs.apptoolkit.app

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Enforces where repositories live and what they are called across every library module.
 *
 * This replaces a hand-maintained list of `interface to implementation` pairs asserted with
 * `isAssignableFrom`. That assertion could not fail: `class FooImpl : Foo` is checked by the
 * compiler, so the test restated the type system while its list silently fell behind — it was
 * missing six repositories and did not notice `MainRepositoryImpl` implementing
 * `NavigationRepository`, or a repository living at a module root with no `domain`/`data` split.
 *
 * Scanning the source tree instead means a new module is covered the moment it is added, and the
 * rules below fail on the drift the old test was meant to catch.
 */
class RepositoryConventionsTest {

    @Test
    fun `repository contracts live in domain repository packages`() {
        val misplaced = libraryMainSources()
            .filter { it.name.endsWith(SUFFIX_CONTRACT) }
            .filterNot { it.parentPath().endsWith(PACKAGE_DOMAIN) }
            .map { it.relativePath() }

        assertThat(misplaced).isEmpty()
    }

    @Test
    fun `repository implementations live in data repository packages`() {
        val misplaced = libraryMainSources()
            .filter { it.name.endsWith(SUFFIX_IMPLEMENTATION) }
            .filterNot { it.parentPath().endsWith(PACKAGE_DATA) }
            .map { it.relativePath() }

        assertThat(misplaced).isEmpty()
    }

    @Test
    fun `every repository implementation is named after the contract it implements`() {
        val contracts: Set<String> = libraryMainSources()
            .filter { it.parentPath().endsWith(PACKAGE_DOMAIN) }
            .map { it.nameWithoutExtension }
            .toSet()

        val orphaned = libraryMainSources()
            .filter { it.name.endsWith(SUFFIX_IMPLEMENTATION) }
            .filterNot { it.nameWithoutExtension.removeSuffix(SUFFIX_IMPL_MARKER) in contracts }
            .map { it.relativePath() }

        assertThat(orphaned).isEmpty()
    }

    private fun libraryMainSources(): List<File> =
        File(repositoryRoot, LIBRARY_DIRECTORY)
            .walkTopDown()
            .filter { it.isFile && it.extension == KOTLIN_EXTENSION }
            .filter { MAIN_SOURCE_SET in it.invariantPath() }
            .toList()

    private fun File.parentPath(): String = parentFile.invariantPath()

    private fun File.invariantPath(): String = path.replace(File.separatorChar, '/')

    private fun File.relativePath(): String = relativeTo(repositoryRoot).invariantPath()

    private companion object {
        const val LIBRARY_DIRECTORY = "library"
        const val MAIN_SOURCE_SET = "/src/main/"
        const val KOTLIN_EXTENSION = "kt"
        const val PACKAGE_DOMAIN = "/domain/repository"
        const val PACKAGE_DATA = "/data/repository"
        const val SUFFIX_IMPL_MARKER = "Impl"
        const val SUFFIX_CONTRACT = "Repository.kt"
        const val SUFFIX_IMPLEMENTATION = "RepositoryImpl.kt"

        /**
         * Gradle runs tests from the module directory, so the repository root has to be walked to
         * rather than assumed — the depth differs between `library/apptoolkit` and any module that
         * later inherits this test.
         */
        val repositoryRoot: File = generateSequence(File("").absoluteFile) { it.parentFile }
            .firstOrNull { File(it, SETTINGS_FILE).isFile }
            ?: error("Could not locate $SETTINGS_FILE above ${File("").absolutePath}")

        const val SETTINGS_FILE = "settings.gradle.kts"
    }
}
