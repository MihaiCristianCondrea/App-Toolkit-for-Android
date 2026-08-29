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
 * Guards the manifest and resource defaults contributed to AppToolkit hosts.
 *
 * The manifest merger folds every library `<application>` attribute into the host, and neither
 * adding nor removing one produces a build error. That makes the boundary invisible in review and
 * expensive to get wrong in both directions. AppToolkit therefore owns the defaults that every
 * host needs while the host keeps merger priority and can override them explicitly. This prevents
 * a toolkit release from silently dropping the AppCompat theme, RTL support or backup rules from
 * every consuming application.
 *
 * Other library modules contribute only the components and permissions they own. The facade is the
 * single exception for common application attributes, themes, colors, locale resources and backup
 * rules. The host links the locale resource because Android does not merge `localeConfig` from a
 * library into the final application manifest.
 */
class ManifestContractTest {

    @Test
    fun `only app toolkit facade declares application attributes`() {
        val offenders = libraryManifests()
            .filterNot { it.invariantPath().endsWith(FACADE_MANIFEST) }
            .mapNotNull { manifest ->
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
    fun `app toolkit facade declares the complete application defaults`() {
        val attributes = APPLICATION_TAG.find(File(repositoryRoot, FACADE_MANIFEST).readText())
            ?.groupValues
            ?.get(1)
            ?.let { ATTRIBUTE_NAME.findAll(it).map { match -> match.groupValues[1] }.toSet() }
            .orEmpty()

        assertThat(attributes).containsExactlyElementsIn(FACADE_APPLICATION_ATTRIBUTES)
    }

    @Test
    fun `app toolkit owns default themes colors locale config and backup rules`() {
        TOOLKIT_DEFAULT_RESOURCES.forEach { relativePath ->
            assertThat(File(repositoryRoot, relativePath).isFile).isTrue()
        }
        FORMER_SAMPLE_DEFAULT_RESOURCES.forEach { relativePath ->
            assertThat(File(repositoryRoot, relativePath).exists()).isFalse()
        }
        assertThat(File(repositoryRoot, SAMPLE_MANIFEST).readText())
            .contains("android:localeConfig=\"@xml/config_locales\"")
    }

    @Test
    fun `toolkit identity placeholders are overridden by the sample host`() {
        val defaultResources = File(repositoryRoot, TOOLKIT_IDENTITY_RESOURCES).readText()
        val sampleResources = File(repositoryRoot, SAMPLE_IDENTITY_RESOURCES).readText()

        TOOLKIT_IDENTITY_DEFAULTS.forEach { (name, value) ->
            assertThat(defaultResources)
                .contains("<string name=\"$name\" translatable=\"false\">$value</string>")
        }
        SAMPLE_IDENTITY_OVERRIDES.forEach { (name, value) ->
            assertThat(sampleResources)
                .contains("<string name=\"$name\" translatable=\"false\">$value</string>")
            assertThat(resourceDefinitions(name))
                .containsExactly(TOOLKIT_IDENTITY_RESOURCES, SAMPLE_IDENTITY_RESOURCES)
        }
        assertThat(resourceDefinitions("copyright")).containsExactly(TOOLKIT_IDENTITY_RESOURCES)
    }

    @Test
    fun `settings shortcut enters through the exported sample launcher`() {
        val shortcut = File(repositoryRoot, SAMPLE_SHORTCUTS).readText()
        val buildScript = File(repositoryRoot, SAMPLE_BUILD_SCRIPT).readText()
        val mainActivity = File(repositoryRoot, SAMPLE_MAIN_ACTIVITY_SOURCE).readText()

        assertThat(shortcut).contains("android:action=\"$OPEN_SETTINGS_ACTION\"")
        assertThat(shortcut).contains("android:targetClass=\"$SAMPLE_MAIN_ACTIVITY\"")
        assertThat(shortcut).contains("android:targetPackage=\"@string/app_package_name\"")
        assertThat(buildScript).contains("applicationId = \"$SAMPLE_APPLICATION_ID\"")
        assertThat(buildScript)
            .contains("resValue(\"string\", \"app_package_name\", releasedApplicationId)")
        assertThat(mainActivity).contains("override fun onNewIntent(intent: Intent)")
        assertThat(mainActivity).contains("setIntent(intent)")
        assertThat(mainActivity).contains("\"$OPEN_SETTINGS_ACTION\"")
        assertThat(mainActivity)
            .contains("openActivity(activityClass = SettingsActivity::class.java)")
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
        // legitimate case, the activity the system opens for VIEW_PERMISSION_USAGE, and it is
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

    private fun resourceDefinitions(resourceName: String): List<String> =
        listOf(LIBRARY_ROOT, SAMPLE_ROOT).flatMap { sourceRoot ->
            File(repositoryRoot, sourceRoot)
                .walkTopDown()
                .filter { file ->
                    file.isFile &&
                        file.extension == "xml" &&
                        MAIN_RESOURCES in file.invariantPath() &&
                        "<string name=\"$resourceName\"" in file.readText()
                }
                .map { it.relativePath() }
                .toList()
        }

    private companion object {
        const val LIBRARY_ROOT = "library"
        const val SAMPLE_ROOT = "sample"
        const val MANIFEST_FILE = "AndroidManifest.xml"
        const val MAIN_SOURCE_SET = "/src/main/"
        const val MAIN_RESOURCES = "/src/main/res/"
        const val BUILD_DIRECTORY = "/build/"
        const val FACADE_MANIFEST = "library/apptoolkit/src/main/AndroidManifest.xml"
        const val SAMPLE_MANIFEST = "sample/app/src/main/AndroidManifest.xml"
        const val INTENT_FILTER = "intent-filter"
        const val UNNAMED_COMPONENT = "<unnamed>"
        const val SETTINGS_FILE = "settings.gradle.kts"
        const val SAMPLE_SHORTCUTS = "sample/app/src/main/res/xml/shortcuts.xml"
        const val SAMPLE_BUILD_SCRIPT = "sample/app/build.gradle.kts"
        const val OPEN_SETTINGS_ACTION = "com.d4rk.android.apps.apptoolkit.action.OPEN_SETTINGS"
        const val SAMPLE_APPLICATION_ID = "com.d4rk.android.apps.apptoolkit"
        const val SAMPLE_MAIN_ACTIVITY =
            "com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.MainActivity"
        const val SAMPLE_MAIN_ACTIVITY_SOURCE =
            "sample/app/src/main/kotlin/com/mihaicristiancondrea/android/apps/apptoolkit/" +
                "app/main/ui/MainActivity.kt"

        val FACADE_APPLICATION_ATTRIBUTES = setOf(
            "android:allowBackup",
            "android:dataExtractionRules",
            "android:enableOnBackInvokedCallback",
            "android:fullBackupContent",
            "android:hardwareAccelerated",
            "android:resizeableActivity",
            "android:supportsRtl",
            "android:theme",
            "android:usesCleartextTraffic",
            "android:windowSoftInputMode",
            "tools:targetApi",
        )

        val TOOLKIT_DEFAULT_RESOURCES = listOf(
            "library/apptoolkit/src/main/res/values/themes.xml",
            "library/apptoolkit/src/main/res/values/colors.xml",
            "library/apptoolkit/src/main/res/drawable-anydpi/ic_shortcut_settings_foreground.xml",
            "library/apptoolkit/src/main/res/xml/backup_rules.xml",
            "library/apptoolkit/src/main/res/xml/config_locales.xml",
            "library/apptoolkit/src/main/res/xml/data_extraction_rules.xml",
        )

        val FORMER_SAMPLE_DEFAULT_RESOURCES = listOf(
            "sample/core/ui/src/main/res/values/themes.xml",
            "sample/core/ui/src/main/res/values/colors.xml",
            "sample/core/ui/src/main/res/drawable-anydpi/ic_shortcut_settings_foreground.xml",
            "sample/app/src/main/res/xml/backup_rules.xml",
            "sample/app/src/main/res/xml/config_locales.xml",
            "sample/app/src/main/res/xml/data_extraction_rules.xml",
        )

        const val TOOLKIT_IDENTITY_RESOURCES =
            "library/core/common/src/main/res/values/untranslatable_strings.xml"
        const val SAMPLE_IDENTITY_RESOURCES =
            "sample/app/src/main/res/values/untranslatable_strings.xml"

        val TOOLKIT_IDENTITY_DEFAULTS = mapOf(
            "app_full_name" to "App Name",
            "app_name" to "App Name",
            "copyright" to "Copyright ©2020-2026, Mihai-Cristian Condrea",
        )

        val SAMPLE_IDENTITY_OVERRIDES = mapOf(
            "app_full_name" to "App Toolkit for Android",
            "app_name" to "App Toolkit",
        )

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
