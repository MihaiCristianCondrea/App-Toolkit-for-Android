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

import android.os.Build
import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

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
    DarkTheme(DisplayCategory.Appearance, R.string.dark_theme, null),
    DynamicColors(DisplayCategory.Appearance, R.string.dynamic_colors, R.string.summary_preference_settings_dynamic_colors),
    BounceButtons(DisplayCategory.AppBehavior, R.string.bounce_buttons, R.string.summary_preference_settings_bounce_buttons),
    StartupPage(DisplayCategory.Navigation, CoreUiR.string.startup_page, R.string.summary_preference_settings_startup_page),
    NavigationLabels(
        DisplayCategory.Navigation,
        R.string.show_labels_on_bottom_bar,
        R.string.summary_preference_settings_show_labels_on_bottom_bar,
    ),
    Language(DisplayCategory.Language, R.string.language, R.string.summary_preference_settings_language),
}

/**
 * The display rows that mean something in this app, from what its declared shell can do, never
 * from the window it is drawn in now or the developer options:
 *
 * - dynamic colours from Android 12;
 * - the startup page where the host offers one and has more than one place to start, counting its
 *   tabs while it does not say how many choices it offers;
 * - the navigation labels where the app shows more than one tab in a bottom bar on some window,
 *   since the bar always labels the selected tab.
 *
 * @param startup The host's startup page support, or null when it binds none.
 * @param sdkInt The Android version the rows are for, the device's by default.
 */
internal fun displayRows(
    capabilities: ShellCapabilities,
    startup: DisplaySettingsProvider?,
    sdkInt: Int = Build.VERSION.SDK_INT,
): List<DisplayRow> = DisplayRow.entries.filter { row ->
    when (row) {
        DisplayRow.DarkTheme, DisplayRow.BounceButtons, DisplayRow.Language -> true
        DisplayRow.DynamicColors -> sdkInt >= Build.VERSION_CODES.S
        DisplayRow.StartupPage -> startup?.offersStartupChoice(capabilities) == true
        DisplayRow.NavigationLabels -> capabilities.hasMultipleTabs && capabilities.usesBottomNavigation
    }
}

private fun DisplaySettingsProvider.offersStartupChoice(capabilities: ShellCapabilities): Boolean =
    supportsStartupPage && (startupPageChoices?.let { it > 1 } ?: capabilities.hasMultipleTabs)
