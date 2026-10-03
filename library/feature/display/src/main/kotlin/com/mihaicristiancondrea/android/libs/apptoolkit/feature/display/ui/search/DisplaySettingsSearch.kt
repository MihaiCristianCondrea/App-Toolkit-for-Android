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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.search

import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.displayRows
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute

/**
 * The display settings rows the settings search finds: exactly the rows the page shows in this
 * app, from the same `displayRows`.
 *
 * @param startup The host's startup page support, or null when it binds none.
 * @param sdkInt The Android version the rows are for, the device's by default.
 */
internal fun displaySettingsSearch(
    startup: DisplaySettingsProvider?,
    sdkInt: Int = Build.VERSION.SDK_INT,
): SettingsSearchProvider = settingsSearchProvider(section = R.string.display, destination = DisplaySettingsRoute) {
    displayRows(capabilities, startup, sdkInt).forEach { row -> preference(row.title, summary = row.summary) }
}
