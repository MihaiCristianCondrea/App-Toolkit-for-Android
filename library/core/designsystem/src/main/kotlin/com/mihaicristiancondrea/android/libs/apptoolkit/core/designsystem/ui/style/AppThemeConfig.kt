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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import androidx.compose.material3.ColorScheme

/**
 * The host's own light and dark schemes for the `default` palette.
 *
 * These are configuration, not state: set them before the first composition, normally in
 * `Application.onCreate`. `AppTheme` reads them when one of its theme inputs changes, so a value
 * assigned while a screen is showing is not picked up until the next theme change.
 */
object AppThemeConfig {
    var customLightScheme: ColorScheme? = null
    var customDarkScheme: ColorScheme? = null
}
