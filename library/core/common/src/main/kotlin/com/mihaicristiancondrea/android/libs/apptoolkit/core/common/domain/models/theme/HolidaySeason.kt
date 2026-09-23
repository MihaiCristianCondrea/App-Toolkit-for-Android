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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds

/**
 * A holiday with its own theme.
 *
 * The date windows live in `LocalDateExtensions` (`isChristmasSeason`, `isHalloweenSeason`).
 *
 * @property paletteId The static palette worn during the holiday.
 */
enum class HolidaySeason(val paletteId: String) {
    CHRISTMAS(StaticPaletteIds.CHRISTMAS),
    HALLOWEEN(StaticPaletteIds.HALLOWEEN),
    ;

    companion object {
        /** The holiday whose palette is [paletteId], or null for an everyday palette. */
        fun fromPaletteId(paletteId: String): HolidaySeason? =
            entries.firstOrNull { it.paletteId == paletteId }
    }
}
