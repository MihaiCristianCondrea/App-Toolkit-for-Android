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

import androidx.compose.runtime.Immutable

/**
 * The stored display preferences the display settings screen shows.
 *
 * @property themeMode One of the `DataStoreNamesConstants.THEME_MODE_*` keys.
 * @property language The stored language tag, empty when the app follows the system.
 * @property startupRoute The route the app opens on, empty while none was chosen.
 */
@Immutable
data class DisplaySettings(
    val themeMode: String,
    val dynamicColors: Boolean,
    val bouncyButtons: Boolean,
    val showBottomBarLabels: Boolean,
    val language: String,
    val startupRoute: String,
)
