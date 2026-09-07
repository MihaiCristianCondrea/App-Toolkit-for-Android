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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.parcelize.Parcelize
import org.junit.jupiter.api.Test

class NavigationItemIconsTest {

    @Parcelize
    private data class TestNavKey(
        override val destinationType: NavigationDestinationType = NavigationDestinationType.TopLevel
    ) : StableNavKey

    @Test
    fun `ToolkitIcon factory functions create correct instances`() {
        val vectorIcon = ToolkitIcon.of(Icons.Outlined.Home)
        assertTrue(vectorIcon is ToolkitIcon.Vector)
        assertEquals(Icons.Outlined.Home, vectorIcon.imageVector)

        val resourceIcon = ToolkitIcon.of(123)
        assertTrue(resourceIcon is ToolkitIcon.Resource)
        assertEquals(123, resourceIcon.resId)

        val animatedIcon = ToolkitIcon.animated(456)
        assertTrue(animatedIcon is ToolkitIcon.AnimatedVector)
        assertEquals(456, animatedIcon.resId)
    }

    @Test
    fun `NavigationDrawerItem secondary constructors support ImageVector and DrawableRes`() {
        val itemVector = NavigationDrawerItem(
            title = 10,
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Outlined.Settings,
            route = "home"
        )
        assertEquals(ToolkitIcon.Vector(Icons.Outlined.Home), itemVector.icon)
        assertEquals(ToolkitIcon.Vector(Icons.Outlined.Settings), itemVector.selectedIcon)

        val itemRes = NavigationDrawerItem(
            title = 10,
            iconResId = 101,
            selectedIconResId = 102,
            route = "settings"
        )
        assertEquals(ToolkitIcon.Resource(101), itemRes.icon)
        assertEquals(ToolkitIcon.Resource(102), itemRes.selectedIcon)

        val itemAnimated = NavigationDrawerItem(
            title = 10,
            animatedIcon = ToolkitIcon.AnimatedVector(201),
            route = "animated"
        )
        assertEquals(ToolkitIcon.AnimatedVector(201), itemAnimated.icon)
    }

    @Test
    fun `BottomBarItem secondary constructors support ImageVector and DrawableRes`() {
        val key = TestNavKey()

        val itemVector = BottomBarItem(
            route = key,
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Outlined.Settings,
            title = 20
        )
        assertEquals(ToolkitIcon.Vector(Icons.Outlined.Home), itemVector.icon)
        assertEquals(ToolkitIcon.Vector(Icons.Outlined.Settings), itemVector.selectedIcon)

        val itemRes = BottomBarItem(
            route = key,
            iconResId = 301,
            selectedIconResId = 302,
            title = 20
        )
        assertEquals(ToolkitIcon.Resource(301), itemRes.icon)
        assertEquals(ToolkitIcon.Resource(302), itemRes.selectedIcon)
    }
    @Test
    fun `single animated icon supplies both navigation states and keeps replay mode`() {
        val animation = ToolkitIcon.animated(
            401,
            replayMode = com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode.Reverse,
        )
        val bottom = BottomBarItem(route = TestNavKey(), animatedIcon = animation, title = 10)
        val drawer = NavigationDrawerItem(title = 10, animatedIcon = animation, route = "tools")
        assertEquals(animation, bottom.icon)
        assertEquals(animation, bottom.selectedIcon)
        assertEquals(animation, drawer.icon)
        assertEquals(animation, drawer.selectedIcon)
    }

}
