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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants

/**
 * Stored theme preferences exposed to the UI layer.
 *
 * Defaults match the Toolkit's first-launch appearance: follow the system theme, dynamic colors
 * enabled, pure-black AMOLED mode off, dynamic palette variant `0`, and [StaticPaletteIds.DEFAULT].
 *
 * @property themeMode Selected theme mode key from [DataStoreNamesConstants].
 * @property dynamicColors Whether dynamic wallpaper colors are enabled when supported.
 * @property amoledMode Whether pure-black dark surfaces are enabled.
 * @property dynamicPaletteVariant Clamped index of the active dynamic palette variant.
 * @property staticPaletteId Identifier of the selected static color palette from [StaticPaletteIds].
 */
data class ThemePreferencesState(
    val themeMode: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    val dynamicColors: Boolean = true,
    val amoledMode: Boolean = false,
    val dynamicPaletteVariant: Int = 0,
    val staticPaletteId: String = StaticPaletteIds.DEFAULT,
)
