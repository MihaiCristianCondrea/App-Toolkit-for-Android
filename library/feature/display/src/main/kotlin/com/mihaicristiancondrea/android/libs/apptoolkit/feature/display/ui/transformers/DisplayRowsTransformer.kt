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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.transformers

import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayRow
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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
): ImmutableList<DisplayRow> = DisplayRow.entries.filter { row ->
    when (row) {
        DisplayRow.DarkTheme, DisplayRow.BounceButtons, DisplayRow.Language -> true
        DisplayRow.DynamicColors -> sdkInt >= Build.VERSION_CODES.S
        DisplayRow.StartupPage -> (startup != null) && offersStartupChoice(startup = startup, capabilities = capabilities)
        DisplayRow.NavigationLabels -> capabilities.hasMultipleTabs && capabilities.usesBottomNavigation
    }
}.toImmutableList()

private fun offersStartupChoice(
    startup: DisplaySettingsProvider,
    capabilities: ShellCapabilities,
): Boolean = startup.supportsStartupPage && (startup.startupPageChoices?.let { it > 1 } ?: capabilities.hasMultipleTabs)
