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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ShellCapabilitiesTest {

    private data object First : NavKey
    private data object Second : NavKey
    private data object Third : NavKey
    private data object Fourth : NavKey
    private data object Article : NavKey
    private data object Custom : NavKey
    private data object Welcome : NavKey

    private val icon = ToolkitIcon.Vector(Icons.Outlined.Home)

    private fun ShellGraphBuilder.tabs(count: Int) {
        // The builder registers the reified type, so each tab needs its concrete key type.
        if (count >= 1) tab(First, 0, icon) {}
        if (count >= 2) tab(Second, 0, icon) {}
        if (count >= 3) tab(Third, 0, icon) {}
        if (count >= 4) tab(Fourth, 0, icon) {}
    }

    /** App A: three tabs, a framed page, no accessories. */
    private val appA = shellGraph(appTitle = 0) {
        tabs(3)
        page<Article>(title = { "Article" }) {}
    }

    @Test
    fun `app A, three tabs with the default policy, uses bottom and wide navigation`() {
        val capabilities = ShellCapabilities.of(appA, ShellLayoutPolicy(contentMaxWidth = 840.dp))

        assertTrue(capabilities.hasTabs)
        assertTrue(capabilities.hasMultipleTabs)
        assertTrue(capabilities.usesBottomNavigation)
        assertTrue(capabilities.usesWideNavigation)
        assertTrue(capabilities.hasShellTopBars)
        assertTrue(capabilities.hasContentWidthLimit)
        assertTrue(capabilities.hasBackNavigation)
        assertFalse(capabilities.hasAccessories)
    }

    @Test
    fun `app B, one tab without accessories or a width limit`() {
        val capabilities = ShellCapabilities.of(shellGraph(appTitle = 0) { tabs(1) })

        assertTrue(capabilities.hasTabs)
        assertFalse(capabilities.hasMultipleTabs)
        assertTrue(capabilities.usesBottomNavigation)
        assertFalse(capabilities.hasContentWidthLimit)
        assertFalse(capabilities.hasAccessories)
        assertFalse(capabilities.hasMultipleStartOptions)
    }

    @Test
    fun `app C, pages only, has no navigation and only the app bars its framed pages draw`() {
        val customOnly = shellGraph(appTitle = 0) {
            page<Custom> {}
            page<Welcome> {}
            start(Custom)
            banner {}
        }
        val withFramedPage = shellGraph(appTitle = 0) {
            page<Custom> {}
            page<Article>(title = { "Article" }) {}
            start(Custom)
        }

        val custom = ShellCapabilities.of(customOnly)
        assertFalse(custom.hasTabs)
        assertFalse(custom.usesBottomNavigation)
        assertFalse(custom.usesWideNavigation)
        assertFalse(custom.hasShellTopBars)
        assertFalse(custom.hasBanner, "a banner docks on the tabs' bar")
        assertFalse(custom.hasAccessories)
        assertTrue(ShellCapabilities.of(withFramedPage).hasShellTopBars)
    }

    @Test
    fun `app D, several tabs with a banner and a player`() {
        val graph = shellGraph(appTitle = 0) {
            tabs(4)
            banner {}
            player(ShellPlayer(isActive = { true }, mini = {}, expanded = {}))
        }

        val capabilities = ShellCapabilities.of(graph)

        assertTrue(capabilities.hasBanner)
        assertTrue(capabilities.hasPlayer)
        assertTrue(capabilities.hasAccessories)
        assertTrue(capabilities.usesWideNavigation)
    }

    @Test
    fun `app F, a policy that never shows a bottom bar`() {
        val capabilities = ShellCapabilities.of(appA, ShellLayoutPolicy(railFrom = 0.dp))

        assertFalse(capabilities.usesBottomNavigation)
        assertTrue(capabilities.usesWideNavigation)
    }

    @Test
    fun `a policy that never shows a rail or drawer`() {
        val phoneOnly = ShellLayoutPolicy(railFrom = Dp.Infinity, expandedRailFrom = Dp.Infinity, permanentDrawerFrom = Dp.Infinity)

        val capabilities = ShellCapabilities.of(appA, phoneOnly)

        assertTrue(capabilities.usesBottomNavigation)
        assertFalse(capabilities.usesWideNavigation)
    }

    @Test
    fun `start screens add start options`() {
        val graph = shellGraph(appTitle = 0) {
            tabs(1)
            page<Welcome> {}
            startScreens(Welcome)
        }

        assertTrue(ShellCapabilities.of(graph).hasMultipleStartOptions)
    }
}
