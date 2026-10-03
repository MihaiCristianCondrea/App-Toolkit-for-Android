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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.search

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.displayRows
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

class DisplaySettingsSearchTest {

    private data object First : NavKey
    private data object Second : NavKey
    private data object Third : NavKey

    private val startup = object : DisplaySettingsProvider {
        override val supportsStartupPage: Boolean = true
    }

    private fun graph(tabs: Int): ShellGraph = shellGraph(appTitle = 0) {
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        listOf(First, Second, Third).take(tabs).forEach { key -> tab(key, 0, icon) {} }
        page<DisplaySettingsRoute> {}
        if (tabs == 0) start(DisplaySettingsRoute)
    }

    private fun context(tabs: Int, policy: ShellLayoutPolicy = ShellLayoutPolicy()): SettingsSearchContext {
        val graph = graph(tabs)
        return SettingsSearchContext(graph, ShellCapabilities.of(graph, policy))
    }

    private fun entry(title: Int, summary: Int? = null) =
        SettingsSearchEntry(title = title, section = R.string.display, destination = DisplaySettingsRoute, summary = summary)

    @Test
    fun `the search lists exactly the rows the page shows, for every app`() {
        val contexts = listOf(
            context(tabs = 0),
            context(tabs = 1),
            context(tabs = 3),
            context(tabs = 3, policy = ShellLayoutPolicy(railFrom = 0.dp)),
        )
        for (context in contexts) {
            for (sdkInt in listOf(Build.VERSION_CODES.R, Build.VERSION_CODES.S)) {
                val page = displayRows(context.capabilities, startup, sdkInt).map { it.title }
                val search = displaySettingsSearch(startup, sdkInt).entries(context).map { it.title }

                assertEquals(page, search, "${context.capabilities} on $sdkInt")
            }
        }
    }

    @Test
    fun `with several tabs on Android 12 every display row is searchable, in the page's order`() {
        assertEquals(
            listOf(
                entry(R.string.dark_theme),
                entry(R.string.dynamic_colors, R.string.summary_preference_settings_dynamic_colors),
                entry(R.string.bounce_buttons, R.string.summary_preference_settings_bounce_buttons),
                entry(CoreUiR.string.startup_page, R.string.summary_preference_settings_startup_page),
                entry(R.string.show_labels_on_bottom_bar, R.string.summary_preference_settings_show_labels_on_bottom_bar),
                entry(R.string.language, R.string.summary_preference_settings_language),
            ),
            displaySettingsSearch(startup, Build.VERSION_CODES.S).entries(context(tabs = 3)),
        )
    }

    @Test
    fun `a page with one tab offers neither the labels nor the startup page`() {
        val titles = displaySettingsSearch(startup, Build.VERSION_CODES.S).entries(context(tabs = 1)).map { it.title }

        assertEquals(listOf(R.string.dark_theme, R.string.dynamic_colors, R.string.bounce_buttons, R.string.language), titles)
    }
}
