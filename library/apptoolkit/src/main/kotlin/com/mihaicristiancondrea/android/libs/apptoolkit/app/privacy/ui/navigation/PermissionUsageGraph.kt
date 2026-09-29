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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.navigation.diagnosticsSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.navigation.permissionsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.navigation.privacySettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.navigation.adsSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR

/**
 * The graph of the screen Android opens from its permission manager: the privacy page, on its own,
 * and the pages it links to. It has no tabs, so back from the privacy page returns to the system's
 * settings rather than into the app.
 */
fun permissionUsageGraph(): ShellGraph = ShellGraphBuilder(appTitle = CommonR.string.app_name).apply {
    privacySettingsPage()
    permissionsPage()
    adsSettingsPage()
    diagnosticsSettingsPage()
    start(PrivacySettingsRoute)
}.build()
