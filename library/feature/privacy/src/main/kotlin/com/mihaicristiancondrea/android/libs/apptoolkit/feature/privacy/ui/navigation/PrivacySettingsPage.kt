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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.navigation

import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.PrivacyScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute

/**
 * Registers the security and privacy page for [PrivacySettingsRoute], a detail of the settings
 * list, unless the app registered its own.
 */
fun ShellGraphBuilder.privacySettingsPage() {
    pageIfAbsent<PrivacySettingsRoute>(
        paneRole = PaneRole.Detail,
        title = { stringResource(R.string.security_and_privacy) },
    ) {
        PrivacyScreen()
    }
}
