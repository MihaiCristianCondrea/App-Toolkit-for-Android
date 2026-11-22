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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SettingsPageTest {

    @Test
    fun `registers SettingsRoute as a list page with title, actions and placeholder`() {
        val graph = shellGraph(appTitle = 0) {
            settingsPage()
            start(key = SettingsRoute)
        }

        assertTrue(graph.contains(key = SettingsRoute))
        val destination = graph.destination(key = SettingsRoute)
        assertEquals(PaneRole.List, destination.paneRole)
        assertNotNull(destination.title)
        assertNotNull(destination.actions)
        assertNotNull(destination.placeholder)
    }

    @Test
    fun `keeps an existing host registration for SettingsRoute`() {
        val graph = shellGraph(appTitle = 0) {
            page<SettingsRoute>(paneRole = PaneRole.None, title = null) {}
            settingsPage()
            start(key = SettingsRoute)
        }

        assertEquals(PaneRole.None, graph.destination(key = SettingsRoute).paneRole)
    }
}
