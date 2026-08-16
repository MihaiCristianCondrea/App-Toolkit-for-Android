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
 * Guards what a library manifest is allowed to contribute to the apps that depend on it.
 *
 * The manifest merger folds every library `<application>` attribute into the host, and neither
 * adding nor removing one produces a build error. That makes the boundary invisible in review and
 * expensive to get wrong in both directions:
 *
 * - 2.0.19 declared ten `<application>` attributes here — `theme`, `supportsRtl`, `allowBackup`,
 *   `usesCleartextTraffic` and others. Hosts inherited them without ever declaring them, so the
 *   library was silently deciding each app's backup policy, RTL support and cleartext policy.
 * - 3.0.0-pre1 dropped all ten. Every host lost them at once. Smart Cleaner crashed opening any
 *   toolkit screen, because without `android:theme` the activities fell back to a platform theme
 *   that is not an AppCompat descendant, and its Arabic and Urdu layouts stopped mirroring because
 *   `supportsRtl` was gone. Nothing failed to compile.
 *
 * The rule that avoids both: a library manifest contributes the components it owns and the
 * permissions its own code needs, and nothing that describes the application as a whole. Those are
 * host decisions. `library/README.md` carries the matching host checklist.
 */
class ManifestContractTest {

    @Test
    fun `library manifests declare no application attributes`() {
        val offenders = libraryManifests().mapNotNull { manifest ->
            val attributes = APPLICATION_TAG.find(manifest.readText())
                ?.groupValues
                ?.get(1)
                ?.let { ATTRIBUTE_NAME.findAll(it).map { match -> match.groupValues[1] }.toList() }
                .orEmpty()

            if (attributes.isEmpty()) null else "${manifest.relativePath()}: $attributes"
        }

        assertThat(offenders).isEmpty()
    }

    @Test
    fun `library components declare whether they are exported`() {
        val offenders = libraryComponents()
            .filterNot { component -> EXPORTED.containsMatchIn(component.declaration) }
            .map { "${it.manifestPath}: ${it.name}" }

        assertThat(offenders).isEmpty()
    }

    @Test
    fun `library components are only exported when they answer an intent filter`() {
        // An exported component is reachable from any other app on the device. The toolkit has one
        // legitimate case — the activity the system opens for VIEW_PERMISSION_USAGE — and it is
        // exported precisely because a filter makes it a system entry point. Anything exported
        // without a filter is reachable by other apps for no reason at all.
        val offenders = libraryComponents()
            .filter { component -> EXPORTED_TRUE.containsMatchIn(component.declaration) }
            .filterNot { component -> INTENT_FILTER in component.declaration }
            .map { "${it.manifestPath}: ${it.name}" }

        assertThat(offenders).isEmpty()
    }

    private data class Component(
        val manifestPath: String,
        val name: String,
        val declaration: String,
    )

    private fun libraryComponents(): List<Component> = libraryManifests().flatMap { manifest ->
        val path = manifest.relativePath()
        COMPONENT_TAG.findAll(manifest.readText()).map { match ->
            Component(
                manifestPath = path,
                name = COMPONENT_NAME.find(match.value)?.groupValues?.get(1)?.substringAfterLast('.')
                    ?: UNNAMED_COMPONENT,
                declaration = match.value,
            )
        }
    }

    private fun libraryManifests(): List<File> = File(repositoryRoot, LIBRARY_ROOT)
        .walkTopDown()
        .filter { it.isFile && it.name == MANIFEST_FILE }
        .filter { MAIN_SOURCE_SET in it.invariantPath() }
        .filterNot { BUILD_DIRECTORY in it.invariantPath() }
        .toList()

    private fun File.invariantPath(): String = path.replace(File.separatorChar, '/')

    private fun File.relativePath(): String = relativeTo(repositoryRoot).invariantPath()

    private companion object {
        const val LIBRARY_ROOT = "library"
        const val MANIFEST_FILE = "AndroidManifest.xml"
        const val MAIN_SOURCE_SET = "/src/main/"
        const val BUILD_DIRECTORY = "/build/"
        const val INTENT_FILTER = "intent-filter"
        const val UNNAMED_COMPONENT = "<unnamed>"
        const val SETTINGS_FILE = "settings.gradle.kts"

        /** The opening `<application …>` tag only; its children are components, checked separately. */
        val APPLICATION_TAG = Regex("""<application([^>]*)>""")
        val ATTRIBUTE_NAME = Regex("""([\w-]+:[\w-]+)\s*=""")

        /** Matches both the self-closing and the wrapping form of a component declaration. */
        val COMPONENT_TAG =
            Regex("""<(activity|service|receiver|provider)\b.*?(?:/>|</\1>)""", RegexOption.DOT_MATCHES_ALL)
        val COMPONENT_NAME = Regex("""android:name="([^"]+)"""")
        val EXPORTED = Regex("""android:exported\s*=""")
        val EXPORTED_TRUE = Regex("""android:exported\s*=\s*"true"""")

        val repositoryRoot: File = generateSequence(File("").absoluteFile) { it.parentFile }
            .firstOrNull { File(it, SETTINGS_FILE).isFile }
            ?: error("Could not locate $SETTINGS_FILE above ${File("").absolutePath}")
    }
}
