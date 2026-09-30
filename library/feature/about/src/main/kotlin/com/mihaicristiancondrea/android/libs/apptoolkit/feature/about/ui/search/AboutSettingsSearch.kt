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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.search

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AboutRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute

/**
 * The About page's rows the settings search finds.
 */
internal val aboutSettingsSearch = SettingsSearchProvider { _ ->
    listOf(
        SettingsSearchEntry(title = R.string.app_info, section = CoreUiR.string.about, destination = AboutRoute),
        SettingsSearchEntry(title = R.string.device_info, section = CoreUiR.string.about, destination = AboutRoute),
        SettingsSearchEntry(
            title = R.string.oss_license_title,
            section = CoreUiR.string.about,
            destination = LicensesRoute,
            summary = R.string.summary_preference_settings_oss,
        ),
    )
}
