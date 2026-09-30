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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute

/**
 * The theme page's rows the settings search finds.
 */
internal val themeSettingsSearch = SettingsSearchProvider { _ ->
    fun entry(title: Int, summary: Int? = null) =
        SettingsSearchEntry(title = title, section = R.string.dark_theme, destination = ThemeSettingsRoute, summary = summary)
    listOf(
        entry(R.string.theme_mode, R.string.summary_dark_theme),
        entry(R.string.amoled_mode),
        entry(R.string.wallpaper_colors),
        entry(R.string.color_palette),
    )
}
