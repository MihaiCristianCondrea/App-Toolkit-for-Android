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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.navigation

import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.ThemeSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.views.weather.WeatherEffectAction
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute

/**
 * Registers the theme settings page for [ThemeSettingsRoute], unless the app registered its own.
 *
 * It opens from the display settings, itself a detail beside the settings list, so it is a page
 * of its own rather than a detail, which would replace the display settings instead of stacking.
 */
fun ShellGraphBuilder.themeSettingsPage() {
    pageIfAbsent<ThemeSettingsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(R.string.dark_theme) },
        // The weather effect, for those who found the About screen's easter egg.
        actions = { WeatherEffectAction() },
    ) {
        ThemeSettingsScreen()
    }
}
