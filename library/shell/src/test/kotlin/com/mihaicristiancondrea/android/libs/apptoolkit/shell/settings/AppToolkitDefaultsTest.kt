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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.NavigationDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppToolkitDefaultsTest {

    @Test
    fun `an empty recipe keeps every Toolkit default`() {
        assertEquals(AppToolkitDefaults(), appToolkitDefaults { })
    }

    @Test
    fun `the recipe sets what it names and keeps the rest`() {
        val defaults = appToolkitDefaults {
            shell {
                backEdgeStyle = BackEdgeStyle.FollowFinger
                tabTransition = TabTransitionStyle.Fade
                hideTopBarOnScroll = true
                hideBottomBarOnScroll = false
                limitContentWidth = false
                navigationBarStyle = NavigationBarStyle.Short
                navigationTint = NavigationTint.OnScroll
            }
            navigation {
                tabTopBarStyle = TopBarStyle.CenterAligned
                childTopBarStyle = TopBarStyle.CenterAligned
                pageTopBarStyle = TopBarStyle.LargeCollapsed
                layoutPolicy = ShellLayoutPolicy(railFrom = 720.dp)
            }
        }

        assertEquals(
            ShellSettings(
                backEdgeStyle = BackEdgeStyle.FollowFinger,
                tabTransition = TabTransitionStyle.Fade,
                hideTopBarOnScroll = true,
                hideBottomBarOnScroll = false,
                limitContentWidth = false,
                navigationBarStyle = NavigationBarStyle.Short,
                navigationTint = NavigationTint.OnScroll,
            ),
            defaults.shell,
        )
        assertEquals(
            NavigationDefaults(
                tabTopBarStyle = TopBarStyle.CenterAligned,
                childTopBarStyle = TopBarStyle.CenterAligned,
                pageTopBarStyle = TopBarStyle.LargeCollapsed,
                layoutPolicy = ShellLayoutPolicy(railFrom = 720.dp),
            ),
            defaults.navigation,
        )
    }

    @Test
    fun `a later block builds on an earlier one`() {
        val defaults = appToolkitDefaults {
            shell { backEdgeStyle = BackEdgeStyle.FollowFinger }
            shell { navigationTint = NavigationTint.None }
        }

        assertEquals(BackEdgeStyle.FollowFinger, defaults.shell.backEdgeStyle)
        assertEquals(NavigationTint.None, defaults.shell.navigationTint)
    }
}
