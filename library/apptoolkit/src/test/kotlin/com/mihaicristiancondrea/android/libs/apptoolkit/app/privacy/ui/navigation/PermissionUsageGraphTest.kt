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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.privacy.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PermissionUsageGraphTest {

    private val graph = permissionUsageGraph()

    @Test
    fun `it opens on the privacy page with no shell under it`() {
        assertEquals(PrivacySettingsRoute, graph.start)
        assertEquals(emptyList(), graph.tabs)
    }

    @Test
    fun `every page the privacy page links to opens inside it`() {
        listOf(PermissionsRoute, AdsSettingsRoute, DiagnosticsSettingsRoute).forEach { key ->
            assertTrue(graph.contains(key))
        }
    }

    @Test
    fun `it offers no start choice, so the developer start option cannot replace the privacy page`() {
        assertEquals(emptyList(), graph.startOptions)
        assertFalse(graph.contains(SettingsRoute))
    }
}
