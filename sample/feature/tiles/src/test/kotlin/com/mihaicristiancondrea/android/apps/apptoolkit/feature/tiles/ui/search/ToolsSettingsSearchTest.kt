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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ToolsSettingsSearchTest {

    private val graph = shellGraph(appTitle = 0) {
        tab(ToolkitTilesRoute, 0, ToolkitIcon.Vector(Icons.Outlined.Home)) {}
        page<ToolsSettingsRoute> {}
    }

    @Test
    fun `every row of the tools settings page opens that page`() {
        fun entry(title: Int, summary: Int) =
            SettingsSearchEntry(title = title, section = R.string.tools_settings_title, destination = ToolsSettingsRoute, summary = summary)

        assertEquals(
            listOf(
                entry(R.string.tools_settings_reset_counter, R.string.tools_settings_reset_counter_summary),
                entry(R.string.tools_settings_expand_all, R.string.tools_settings_expand_all_summary),
                entry(R.string.tools_settings_collapse_all, R.string.tools_settings_collapse_all_summary),
            ),
            toolsSettingsSearch.entries(SettingsSearchContext(graph)),
        )
    }
}
