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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.search

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute

/**
 * The theme page's rows the settings search finds. Their labels are the ones the page draws, from
 * `:library:core:ui`; only the page's own title lives in this module.
 */
internal val themeSettingsSearch = settingsSearchProvider(section = R.string.dark_theme, destination = ThemeSettingsRoute) {
    preference(CoreUiR.string.theme_mode, summary = CoreUiR.string.summary_dark_theme)
    preference(CoreUiR.string.amoled_mode)
    preference(CoreUiR.string.wallpaper_colors)
    preference(CoreUiR.string.color_palette)
}
