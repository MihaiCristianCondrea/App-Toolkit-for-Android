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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.search

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider

/**
 * The tools settings rows the Toolkit's settings search finds, declared the way any app declares
 * its own settings pages. `tilesModule` binds it under the name `tools`.
 */
internal val toolsSettingsSearch = settingsSearchProvider(
    section = R.string.tools_settings_title,
    destination = ToolsSettingsRoute,
) {
    preference(R.string.tools_settings_reset_counter, summary = R.string.tools_settings_reset_counter_summary)
    preference(R.string.tools_settings_expand_all, summary = R.string.tools_settings_expand_all_summary)
    preference(R.string.tools_settings_collapse_all, summary = R.string.tools_settings_collapse_all_summary)
}
