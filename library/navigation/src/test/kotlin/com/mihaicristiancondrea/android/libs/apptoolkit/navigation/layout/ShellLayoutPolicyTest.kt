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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShellLayoutPolicyTest {

    private val widths: List<Dp> = (0..3000 step 10).map { it.dp }

    private fun ShellLayoutPolicy.resolvedModes(): Set<ShellLayoutMode> =
        widths.mapTo(HashSet()) { resolve(ShellLayoutMode.Auto, it) }

    private val policies = listOf(
        ShellLayoutPolicy(),
        ShellLayoutPolicy(railFrom = 0.dp),
        ShellLayoutPolicy(permanentDrawerFrom = 0.dp),
        ShellLayoutPolicy(railFrom = Dp.Infinity, expandedRailFrom = Dp.Infinity, permanentDrawerFrom = Dp.Infinity),
        ShellLayoutPolicy(railFrom = Dp.Infinity, expandedRailFrom = Dp.Infinity),
        ShellLayoutPolicy(railFrom = 900.dp, expandedRailFrom = Dp.Infinity, permanentDrawerFrom = Dp.Infinity),
    )

    @Test
    fun `the bottom bar is reachable exactly when some width resolves to it`() {
        for (policy in policies) {
            assertEquals(ShellLayoutMode.BottomBar in policy.resolvedModes(), policy.reachesBottomBar, "$policy")
        }
    }

    @Test
    fun `wide navigation is reachable exactly when some width resolves to a rail or the drawer`() {
        val wide = setOf(ShellLayoutMode.Rail, ShellLayoutMode.ExpandedRail, ShellLayoutMode.PermanentDrawer)
        for (policy in policies) {
            assertEquals(policy.resolvedModes().any { it in wide }, policy.reachesWideNavigation, "$policy")
        }
    }

    @Test
    fun `the default policy reaches both`() {
        assertEquals(true, ShellLayoutPolicy().reachesBottomBar)
        assertEquals(true, ShellLayoutPolicy().reachesWideNavigation)
    }
}
