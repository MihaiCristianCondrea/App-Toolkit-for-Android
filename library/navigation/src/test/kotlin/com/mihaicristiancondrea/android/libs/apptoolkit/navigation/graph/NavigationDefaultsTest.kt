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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NavigationDefaultsTest {

    private data object Home : NavKey
    private data object Item : NavKey
    private data object Article : NavKey
    private data object Player : NavKey
    private data object ToolkitPage : NavKey

    private val icon = ToolkitIcon.Vector(Icons.Outlined.Home)

    private val appDefaults = NavigationDefaults(
        tabTopBarStyle = TopBarStyle.CenterAligned,
        childTopBarStyle = TopBarStyle.CenterAligned,
        pageTopBarStyle = TopBarStyle.LargeCollapsed,
        layoutPolicy = ShellLayoutPolicy(railFrom = 720.dp),
    )

    private fun graph(defaults: NavigationDefaults): ShellGraph = ShellGraphBuilder(appTitle = 0, defaults = defaults).apply {
        tab(Home, 0, icon) {}
        child<Item>(title = { "" }) {}
        page<Article>(title = { "" }) {}
        page<Player>(topBar = TopBarStyle.Hidden) {}
        pageIfAbsent<ToolkitPage>(paneRole = PaneRole.None, title = { "" }) {}
    }.build()

    @Test
    fun `without app defaults destinations keep the Toolkit's styles`() {
        val graph = graph(NavigationDefaults())

        assertEquals(TopBarStyle.Small, graph.destination(Home).topBar)
        assertEquals(TopBarStyle.Small, graph.destination(Item).topBar)
        assertEquals(TopBarStyle.Large, graph.destination(Article).topBar)
        assertEquals(TopBarStyle.Large, graph.destination(ToolkitPage).topBar)
    }

    @Test
    fun `destinations that declare no style follow the app's defaults`() {
        val graph = graph(appDefaults)

        assertEquals(TopBarStyle.CenterAligned, graph.destination(Home).topBar)
        assertEquals(TopBarStyle.CenterAligned, graph.destination(Item).topBar)
        assertEquals(TopBarStyle.LargeCollapsed, graph.destination(Article).topBar)
        assertEquals(TopBarStyle.LargeCollapsed, graph.destination(ToolkitPage).topBar)
    }

    @Test
    fun `a destination's own style wins over the app's defaults`() {
        assertEquals(TopBarStyle.Hidden, graph(appDefaults).destination(Player).topBar)
    }

    @Test
    fun `the graph carries the app's layout policy`() {
        assertEquals(appDefaults.layoutPolicy, graph(appDefaults).defaults.layoutPolicy)
    }
}
