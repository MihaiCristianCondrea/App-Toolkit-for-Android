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

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.junit.jupiter.api.Test

class PermissionUsageGraphTest {

    private val graph = permissionUsageGraph()

    @Test
    fun `it opens on the privacy page with no shell under it`() {
        assertThat(graph.start).isEqualTo(PrivacySettingsRoute)
        assertThat(graph.tabs).isEmpty()
    }

    @Test
    fun `every page the privacy page links to opens inside it`() {
        listOf(PermissionsRoute, AdsSettingsRoute, DiagnosticsSettingsRoute).forEach { key ->
            assertThat(graph.contains(key)).isTrue()
        }
    }

    @Test
    fun `it offers no start choice, so the developer start option cannot replace the privacy page`() {
        assertThat(graph.startOptions).isEmpty()
        assertThat(graph.contains(SettingsRoute)).isFalse()
    }
}
