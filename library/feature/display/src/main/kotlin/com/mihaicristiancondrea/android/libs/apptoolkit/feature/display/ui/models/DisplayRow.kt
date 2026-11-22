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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models

import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.transformers.displayRows

/**
 * A row of the display settings page, in the order the page shows them. The page draws the rows
 * [displayRows] gives, and the settings search lists the same rows, so the two never differ.
 *
 * @property category The heading the row sits under.
 * @property title The row's title, on the page and in the search.
 * @property summary The summary the search matches; null where the page's summary changes with
 * the setting.
 */
internal enum class DisplayRow(
    val category: DisplayCategory,
    @param:StringRes val title: Int,
    @param:StringRes val summary: Int?,
) {
    DarkTheme(
        category = DisplayCategory.Appearance,
        title = R.string.dark_theme,
        summary = null,
    ),
    DynamicColors(
        category = DisplayCategory.Appearance,
        title = R.string.dynamic_colors,
        summary = R.string.summary_preference_settings_dynamic_colors,
    ),
    BounceButtons(
        category = DisplayCategory.AppBehavior,
        title = R.string.bounce_buttons,
        summary = R.string.summary_preference_settings_bounce_buttons,
    ),
    StartupPage(
        category = DisplayCategory.Navigation,
        title = CoreUiR.string.startup_page,
        summary = R.string.summary_preference_settings_startup_page,
    ),
    NavigationLabels(
        category = DisplayCategory.Navigation,
        title = R.string.show_labels_on_bottom_bar,
        summary = R.string.summary_preference_settings_show_labels_on_bottom_bar,
    ),
    Language(
        category = DisplayCategory.Language,
        title = R.string.language,
        summary = R.string.summary_preference_settings_language,
    ),
}
