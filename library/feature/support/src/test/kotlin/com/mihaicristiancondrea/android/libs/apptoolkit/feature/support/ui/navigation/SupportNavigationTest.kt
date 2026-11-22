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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportScreen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SupportNavigationTest {

    @Test
    fun `supportScreen registers SupportScreen as a standalone page with a title`() {
        val graph = shellGraph(appTitle = 0) {
            supportScreen()
            start(key = SupportScreen)
        }

        assertTrue(graph.contains(key = SupportScreen))
        val destination = graph.destination(key = SupportScreen)
        assertEquals(PaneRole.None, destination.paneRole)
        assertNotNull(destination.title)
    }

    @Test
    fun `supportScreen preserves a host page registered for SupportScreen`() {
        val graph = shellGraph(appTitle = 0) {
            page<SupportScreen>(paneRole = PaneRole.Detail, title = null) {}
            supportScreen()
            start(key = SupportScreen)
        }

        assertEquals(PaneRole.Detail, graph.destination(key = SupportScreen).paneRole)
    }
}
