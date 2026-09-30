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

import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute

/**
 * The privacy page's rows the settings search finds. Each opens the page the row opens, rather
 * than the privacy page it sits on.
 */
internal val privacySettingsSearch = SettingsSearchProvider { _ ->
    fun entry(title: Int, summary: Int, destination: NavKey) =
        SettingsSearchEntry(title = title, section = R.string.security_and_privacy, destination = destination, summary = summary)
    listOf(
        entry(R.string.permissions, R.string.summary_preference_settings_permissions, PermissionsRoute),
        entry(R.string.ads, R.string.summary_preference_settings_ads, AdsSettingsRoute),
        entry(R.string.usage_and_diagnostics, R.string.summary_preference_settings_usage_and_diagnostics, DiagnosticsSettingsRoute),
    )
}
