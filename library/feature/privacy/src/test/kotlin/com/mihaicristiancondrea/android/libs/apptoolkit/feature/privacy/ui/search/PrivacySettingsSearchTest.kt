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
package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute

class PrivacySettingsSearchTest {

    private data object Home : NavKey

    private val graph = shellGraph(appTitle = 0) { tab(Home, 0, ToolkitIcon.Vector(Icons.Outlined.Home)) {} }

    @Test
    fun `each privacy row opens the page the row opens`() {
        fun entry(title: Int, summary: Int, destination: NavKey) =
            SettingsSearchEntry(title = title, section = R.string.security_and_privacy, destination = destination, summary = summary)

        assertEquals(
            listOf(
                entry(R.string.permissions, R.string.summary_preference_settings_permissions, PermissionsRoute),
                entry(R.string.ads, R.string.summary_preference_settings_ads, AdsSettingsRoute),
                entry(R.string.usage_and_diagnostics, R.string.summary_preference_settings_usage_and_diagnostics, DiagnosticsSettingsRoute),
            ),
            privacySettingsSearch.entries(SettingsSearchContext(graph)),
        )
    }}
