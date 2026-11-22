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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.StartupScreen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StartupNavigationTest {

    @Test
    fun `startupScreen registers StartupScreen without a title and adds it to startScreens`() {
        val graph = shellGraph(appTitle = 0) {
            startupScreen()
            start(key = StartupScreen)
        }

        assertTrue(graph.contains(key = StartupScreen))
        val destination = graph.destination(key = StartupScreen)
        assertEquals(PaneRole.None, destination.paneRole)
        assertNull(destination.title)
        assertTrue(StartupScreen in graph.startScreens)
    }

    @Test
    fun `startupScreen preserves a host page registered for StartupScreen`() {
        val graph = shellGraph(appTitle = 0) {
            page<StartupScreen>(paneRole = PaneRole.Detail, title = { "Custom" }) {}
            startupScreen()
            start(key = StartupScreen)
        }

        val destination = graph.destination(key = StartupScreen)
        assertEquals(PaneRole.Detail, destination.paneRole)
        assertNotNull(destination.title)
        assertTrue(StartupScreen in graph.startScreens)
    }
}
