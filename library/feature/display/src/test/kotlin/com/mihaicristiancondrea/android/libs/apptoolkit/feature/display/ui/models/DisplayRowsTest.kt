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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models

import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisplayRowsTest {

    private fun capabilities(
        tabs: Int,
        bottomBar: Boolean = true,
        wide: Boolean = true,
    ) = ShellCapabilities(
        tabCount = tabs,
        startOptionCount = tabs,
        declaresBanner = false,
        declaresPlayer = false,
        hasFramedPages = true,
        hasBackNavigation = true,
        layoutReachesBottomBar = bottomBar,
        layoutReachesWideNavigation = wide,
        hasContentWidthLimit = false,
    )

    private fun startup(supports: Boolean = true, choices: Int? = null) = object : DisplaySettingsProvider {
        override val supportsStartupPage: Boolean = supports
        override val startupPageChoices: Int? = choices
    }

    private fun rows(
        tabs: Int,
        bottomBar: Boolean = true,
        startup: DisplaySettingsProvider? = startup(choices = 2),
        sdkInt: Int = Build.VERSION_CODES.S,
    ) = displayRows(capabilities(tabs, bottomBar), startup, sdkInt)

    @Test
    fun `app A, three tabs with a startup page, shows every row`() {
        assertEquals(DisplayRow.entries, rows(tabs = 3))
    }

    @Test
    fun `app B, one tab, has no navigation labels and keeps the tab fallback for the startup page`() {
        val rows = rows(tabs = 1, startup = startup(choices = null))

        assertFalse(DisplayRow.NavigationLabels in rows)
        assertFalse(DisplayRow.StartupPage in rows)
    }

    @Test
    fun `app C, no tabs, has no navigation heading at all`() {
        val rows = rows(tabs = 0, startup = startup(choices = null))

        assertEquals(
            listOf(DisplayRow.DarkTheme, DisplayRow.DynamicColors, DisplayRow.BounceButtons, DisplayRow.Language),
            rows,
        )
        assertTrue(rows.none { it.category == DisplayCategory.Navigation })
    }

    @Test
    fun `app F, tabs that never use a bottom bar, has no navigation labels`() {
        assertFalse(DisplayRow.NavigationLabels in rows(tabs = 3, bottomBar = false))
    }

    @Test
    fun `navigation labels need more than one tab, since the bar always labels the selected one`() {
        assertFalse(DisplayRow.NavigationLabels in rows(tabs = 1))
        assertTrue(DisplayRow.NavigationLabels in rows(tabs = 2))
    }

    @Test
    fun `the startup page follows the host's own count of choices`() {
        assertTrue(DisplayRow.StartupPage in rows(tabs = 1, startup = startup(choices = 3)))
        assertFalse(DisplayRow.StartupPage in rows(tabs = 3, startup = startup(choices = 1)))
    }

    @Test
    fun `without a count, the startup page shows when there is more than one tab`() {
        assertTrue(DisplayRow.StartupPage in rows(tabs = 2, startup = startup(choices = null)))
        assertFalse(DisplayRow.StartupPage in rows(tabs = 1, startup = startup(choices = null)))
    }

    @Test
    fun `app H, a host without a startup page, never shows the row`() {
        assertFalse(DisplayRow.StartupPage in rows(tabs = 3, startup = null))
        assertFalse(DisplayRow.StartupPage in rows(tabs = 3, startup = startup(supports = false, choices = 3)))
    }

    @Test
    fun `dynamic colours need Android 12`() {
        assertFalse(DisplayRow.DynamicColors in rows(tabs = 3, sdkInt = Build.VERSION_CODES.R))
        assertTrue(DisplayRow.DynamicColors in rows(tabs = 3, sdkInt = Build.VERSION_CODES.S))
    }

    @Test
    fun `rows keep the page's order, grouped under their headings`() {
        val categories = rows(tabs = 3).map { it.category }

        assertEquals(categories.sortedBy { it.ordinal }, categories)
    }
}
