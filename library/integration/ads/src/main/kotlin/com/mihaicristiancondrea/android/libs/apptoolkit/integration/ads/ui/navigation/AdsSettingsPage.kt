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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.navigation

import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.AdsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute

/**
 * Registers the ads settings page for [AdsSettingsRoute], unless the app registered its own.
 *
 * It opens from the privacy page, itself a detail beside the settings list, so it is a page of its
 * own rather than a detail, which would replace the privacy page instead of stacking.
 */
fun ShellGraphBuilder.adsSettingsPage() {
    pageIfAbsent<AdsSettingsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(R.string.ads) },
    ) {
        AdsSettingsScreen()
    }
}
