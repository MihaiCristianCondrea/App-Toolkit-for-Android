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

package com.d4rk.android.libs.apptoolkit.app.theme.ui

import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds

/**
 * Filters seasonal palette options so they are available only during their seasonal windows or
 * when the user currently has them selected.
 */
internal fun filterSeasonalStaticPalettes(
    baseOptions: List<String>,
    isChristmasSeason: Boolean,
    isHalloweenSeason: Boolean,
    selectedPaletteId: String
): List<String> {
    return baseOptions.filter { id ->
        when (id) {
            StaticPaletteIds.CHRISTMAS -> isChristmasSeason || selectedPaletteId == StaticPaletteIds.CHRISTMAS
            StaticPaletteIds.HALLOWEEN -> isHalloweenSeason || selectedPaletteId == StaticPaletteIds.HALLOWEEN
            else -> true
        }
    }
}
