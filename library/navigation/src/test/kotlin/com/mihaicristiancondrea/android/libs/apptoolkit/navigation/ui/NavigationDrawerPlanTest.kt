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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NavigationDrawerPlanTest {

    private fun item(route: String) = NavigationDrawerItem(
        title = 1,
        icon = Icons.Outlined.Android,
        selectedIcon = Icons.Outlined.Android,
        route = route,
    )

    private val standard = NavigationDrawerRoutes.StandardRoutes

    private val settings = item(route = NavigationDrawerRoutes.ROUTE_SETTINGS)
    private val share = item(route = NavigationDrawerRoutes.ROUTE_SHARE)
    private val components = item(route = "components")
    private val trash = item(route = "trash")

    @Test
    fun `app destinations take the top and standard entries drop to the bottom`() {
        val plan = navigationDrawerPlan(
            items = listOf(components, trash, settings, share),
            pinnedRoutes = standard,
            pinStandardRoutes = true,
        )

        assertEquals(listOf(components, trash), plan.leading)
        assertEquals(listOf(settings, share), plan.footer)
        assertTrue(plan.pinFooterToBottom)
    }

    @Test
    fun `a drawer of only standard entries renders as one top aligned block`() {
        val items = listOf(settings, share)

        val plan = navigationDrawerPlan(
            items = items,
            pinnedRoutes = standard,
            pinStandardRoutes = true,
        )

        assertEquals(emptyList<NavigationDrawerItem>(), plan.leading)
        assertEquals(items, plan.footer)
        assertFalse(plan.pinFooterToBottom)
    }

    @Test
    fun `pinning off keeps every item in the order it was given`() {
        val items = listOf(components, settings, trash, share)

        val plan = navigationDrawerPlan(
            items = items,
            pinnedRoutes = standard,
            pinStandardRoutes = false,
        )

        assertEquals(emptyList<NavigationDrawerItem>(), plan.leading)
        assertEquals(items, plan.footer)
        assertFalse(plan.pinFooterToBottom)
    }

    @Test
    fun `an empty pinned set leaves the order alone even while pinning is on`() {
        val items = listOf(components, settings, share)

        val plan = navigationDrawerPlan(
            items = items,
            pinnedRoutes = emptySet(),
            pinStandardRoutes = true,
        )

        assertEquals(items, plan.leading)
        assertEquals(emptyList<NavigationDrawerItem>(), plan.footer)
        assertTrue(plan.pinFooterToBottom)
    }

    @Test
    fun `an empty drawer plans nothing`() {
        val plan = navigationDrawerPlan(
            items = emptyList(),
            pinnedRoutes = standard,
            pinStandardRoutes = true,
        )

        assertEquals(emptyList<NavigationDrawerItem>(), plan.leading)
        assertEquals(emptyList<NavigationDrawerItem>(), plan.footer)
        assertFalse(plan.pinFooterToBottom)
    }
}
