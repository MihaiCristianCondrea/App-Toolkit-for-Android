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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PermissionsPageTest {

    @Test
    fun `registers PermissionsRoute as a standalone page with a title`() {
        val graph = shellGraph(appTitle = 0) {
            permissionsPage()
            start(key = PermissionsRoute)
        }

        assertTrue(graph.contains(key = PermissionsRoute))
        val destination = graph.destination(key = PermissionsRoute)
        assertEquals(PaneRole.None, destination.paneRole)
        assertNotNull(destination.title)
    }

    @Test
    fun `keeps an existing host registration for PermissionsRoute`() {
        val graph = shellGraph(appTitle = 0) {
            page<PermissionsRoute>(paneRole = PaneRole.Detail, title = null) {}
            permissionsPage()
            start(key = PermissionsRoute)
        }

        assertEquals(PaneRole.Detail, graph.destination(key = PermissionsRoute).paneRole)
    }
}
