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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SettingsSearchIndexTest {

    private data object Home : NavKey
    private data object Display : NavKey
    private data object Reader : NavKey
    private data object ReaderTheme : NavKey
    private data object Missing : NavKey

    private val graph: ShellGraph = shellGraph(appTitle = 0) {
        tab(Home, 0, ToolkitIcon.Vector(Icons.Outlined.Home)) {}
        page<Display> {}
        page<Reader> {}
        page<ReaderTheme> {}
    }

    private val strings = mapOf(
        DISPLAY to "Display",
        DARK_THEME to "Dark theme",
        BOUNCE to "Bounce buttons",
        BOUNCE_SUMMARY to "Buttons spring back when pressed",
        READER to "Lecteur",
        FONT_SIZE to "Taille de police",
        FONT_SIZE_SUMMARY to "Plus grand ou plus petit",
        READER_THEME to "Thème de lecture",
        ORPHAN to "Orphan row",
    )

    private val displaySearch = settingsSearchProvider(section = DISPLAY, destination = Display) {
        preference(DARK_THEME)
        preference(BOUNCE, summary = BOUNCE_SUMMARY)
    }

    private val readerSearch = settingsSearchProvider(section = READER, destination = Reader) {
        preference(FONT_SIZE, summary = FONT_SIZE_SUMMARY)
        preference(READER_THEME, destination = ReaderTheme)
    }

    private val rootConfig = SettingsConfig(
        categories = listOf(
            SettingsCategory(
                preferences = listOf(
                    SettingsPreference(key = "notifications", title = "Notifications", summary = "Alerts and sounds", destination = Display),
                    SettingsPreference(key = "about", title = "About", summary = "Version and licences", destination = Reader),
                ),
            ),
        ),
    )

    private fun index(
        config: SettingsConfig = rootConfig,
        providers: List<SettingsSearchProvider> = listOf(displaySearch, readerSearch),
        string: (Int) -> String = { strings.getValue(it) },
        onUnregistered: (NavKey, String?, Boolean) -> Unit = { _, _, _ -> },
    ) = SettingsSearchIndex { searchRows(config, providers, SettingsSearchContext(graph), string, onUnregistered) }

    private fun SettingsSearchIndex.titles(query: String): List<String?> = matching(query).map { it.title }

    @Test
    fun `the host's own rows are found by title and summary`() {
        assertEquals(listOf("Notifications"), index().titles("notif"))
        assertEquals(listOf("About"), index().titles("licences"))
    }

    @Test
    fun `a page's rows are found by title, summary and section`() {
        assertEquals(listOf("Taille de police"), index().titles("taille"))
        assertEquals(listOf("Taille de police"), index().titles("petit"))
        assertEquals(listOf("Taille de police", "Thème de lecture"), index().titles("lecteur"))
    }

    @Test
    fun `every word must match, in any order`() {
        assertEquals(listOf("Bounce buttons"), index().titles("display bounce"))
        assertEquals(listOf("Bounce buttons"), index().titles("  BOUNCE   display "))
        assertEquals(emptyList(), index().titles("display lecteur"))
    }

    @Test
    fun `case and accents are ignored`() {
        assertEquals(listOf("Thème de lecture"), index().titles("THEME DE"))
        assertEquals(listOf("Dark theme", "Thème de lecture"), index().titles("thème"))
    }

    @Test
    fun `a blank query finds nothing`() {
        assertEquals(emptyList(), index().titles("   "))
    }

    @Test
    fun `a result opens its page, or the page its row names`() {
        val results = index().matching("lecteur")

        assertEquals(listOf<NavKey?>(Reader, ReaderTheme), results.map { it.destination })
        assertTrue(results.all { it.summary == "Lecteur" })
    }

    @Test
    fun `app and Toolkit pages are searched together, in the order they are bound`() {
        val appFirst = index(providers = listOf(readerSearch, displaySearch)).titles("e")

        assertEquals(listOf("Taille de police", "Thème de lecture", "Dark theme", "Bounce buttons"), appFirst.takeLast(4))
    }

    @Test
    fun `a page's row whose destination is not registered is left out and reported`() {
        val reported = mutableListOf<Triple<NavKey, String?, Boolean>>()
        val orphan = settingsSearchProvider(section = DISPLAY, destination = Missing) { preference(ORPHAN) }

        val results = index(providers = listOf(orphan), onUnregistered = { key, title, kept -> reported += Triple(key, title, kept) })
            .titles("orphan")

        assertEquals(emptyList(), results)
        assertEquals(listOf(Triple<NavKey, String?, Boolean>(Missing, "Orphan row", false)), reported)
    }

    @Test
    fun `a host row that only opens an unregistered page is left out`() {
        val config = SettingsConfig(
            categories = listOf(SettingsCategory(preferences = listOf(SettingsPreference(title = "Orphan", destination = Missing)))),
        )
        val reported = mutableListOf<Boolean>()

        val results = index(config = config, onUnregistered = { _, _, kept -> reported += kept }).titles("orphan")

        assertEquals(emptyList(), results)
        assertEquals(listOf(false), reported)
    }

    @Test
    fun `a host row with an action keeps it, without an unregistered fallback`() {
        val action = { true }
        val row = SettingsPreference(title = "Notifications", destination = Missing, action = action)
        val config = SettingsConfig(categories = listOf(SettingsCategory(preferences = listOf(row))))
        val reported = mutableListOf<Boolean>()

        val result = index(config = config, onUnregistered = { _, _, kept -> reported += kept }).matching("notifications").single()

        assertSame(action, result.action)
        assertNull(result.destination)
        assertEquals(listOf(true), reported)
    }

    @Test
    fun `a host row with an action and a registered fallback is unchanged`() {
        val row = SettingsPreference(title = "Notifications", destination = Display, action = { false })
        val config = SettingsConfig(categories = listOf(SettingsCategory(preferences = listOf(row))))

        assertSame(row, index(config = config).matching("notifications").single())
    }

    @Test
    fun `a host row without a destination stays searchable`() {
        val row = SettingsPreference(title = "Rate the app", action = { true })
        val config = SettingsConfig(categories = listOf(SettingsCategory(preferences = listOf(row))))

        assertSame(row, index(config = config).matching("rate").single())
    }

    @Test
    fun `the same row from two pages appears once, the first page's`() {
        val again = settingsSearchProvider(section = READER, destination = Display) { preference(DARK_THEME) }

        val results = index(providers = listOf(displaySearch, again)).matching("dark")

        assertEquals(1, results.size)
        assertEquals("Display", results.single().summary)
    }

    @Test
    fun `a summary that cannot be resolved is not searched, and the row still is`() {
        val broken = settingsSearchProvider(section = DISPLAY, destination = Display) { preference(DARK_THEME, summary = MISSING_STRING) }

        assertEquals(listOf("Dark theme"), index(providers = listOf(broken)).titles("dark"))
    }

    @Test
    fun `the text follows the strings it is built with, as after a locale change`() {
        val english = mapOf(READER to "Reader", FONT_SIZE to "Font size", FONT_SIZE_SUMMARY to "Larger or smaller", READER_THEME to "Reading theme")

        val rebuilt = index(config = SettingsConfig(), providers = listOf(readerSearch), string = { english.getValue(it) })

        assertEquals(listOf("Font size"), rebuilt.titles("font"))
        assertEquals(emptyList(), rebuilt.titles("taille"))
    }

    @Test
    fun `every provider lists its rows for the capabilities of the app searched`() {
        val severalTabsOnly = settingsSearchProvider(section = DISPLAY, destination = Display) {
            if (capabilities.hasMultipleTabs) preference(DARK_THEME)
            preference(BOUNCE)
        }

        val titles = index(providers = listOf(severalTabsOnly)).titles("display")

        assertEquals(listOf("Bounce buttons"), titles)
    }

    @Test
    fun `rows are built on the first search only`() {
        var built = 0
        val index = SettingsSearchIndex {
            built++
            searchRows(rootConfig, listOf(displaySearch), SettingsSearchContext(graph), { strings.getValue(it) })
        }

        assertEquals(0, built)
        index.matching("dark")
        index.matching("bounce")
        assertEquals(1, built)
    }

    private companion object {
        const val DISPLAY = 1
        const val DARK_THEME = 2
        const val BOUNCE = 3
        const val BOUNCE_SUMMARY = 4
        const val READER = 5
        const val FONT_SIZE = 6
        const val FONT_SIZE_SUMMARY = 7
        const val READER_THEME = 8
        const val ORPHAN = 9
        const val MISSING_STRING = 99
    }
}
