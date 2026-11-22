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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.AdsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.DiagnosticsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.PermissionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.PrivacySettingsScreen

/**
 * The privacy page's rows the settings search finds. Each opens the page the row opens, rather
 * than the privacy page it sits on.
 */
internal val privacySettingsSearch = settingsSearchProvider(
    section = R.string.security_and_privacy,
    destination = PrivacySettingsScreen,
) {
    preference(
        title = R.string.permissions,
        summary = R.string.summary_preference_settings_permissions,
        destination = PermissionsScreen,
    )
    preference(
        title = R.string.ads,
        summary = R.string.summary_preference_settings_ads,
        destination = AdsSettingsScreen,
    )
    preference(
        title = R.string.usage_and_diagnostics,
        summary = R.string.summary_preference_settings_usage_and_diagnostics,
        destination = DiagnosticsSettingsScreen,
    )
}
