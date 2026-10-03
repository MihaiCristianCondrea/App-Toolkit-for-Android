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
package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

class ThemeSettingsSearchTest {

    private data object Home : NavKey

    private val graph = shellGraph(appTitle = 0) { tab(Home, 0, ToolkitIcon.Vector(Icons.Outlined.Home)) {} }

    @Test
    fun `the theme page's rows open the theme page`() {
        fun entry(title: Int, summary: Int? = null) =
            SettingsSearchEntry(title = title, section = R.string.dark_theme, destination = ThemeSettingsRoute, summary = summary)

        assertEquals(
            listOf(
                entry(CoreUiR.string.theme_mode, CoreUiR.string.summary_dark_theme),
                entry(CoreUiR.string.amoled_mode),
                entry(CoreUiR.string.wallpaper_colors),
                entry(CoreUiR.string.color_palette),
            ),
            themeSettingsSearch.entries(SettingsSearchContext(graph)),
        )
    }}
