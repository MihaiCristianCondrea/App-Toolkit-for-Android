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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SeasonalPaletteFilterTest {

    private val baseOptions: List<String> = StaticPaletteIds.withDefault

    @Test
    fun `seasonal palettes should be hidden when not in season and not selected`() {
        val filtered = filterSeasonalStaticPalettes(
            baseOptions = baseOptions,
            isChristmasSeason = false,
            isHalloweenSeason = false,
            selectedPaletteId = StaticPaletteIds.DEFAULT
        )

        val expected = baseOptions.filterNot {
            it == StaticPaletteIds.CHRISTMAS || it == StaticPaletteIds.HALLOWEEN
        }

        assertEquals(expected, filtered)
    }

    @Test
    fun `selected halloween palette remains available outside of season`() {
        val filtered = filterSeasonalStaticPalettes(
            baseOptions = baseOptions,
            isChristmasSeason = false,
            isHalloweenSeason = false,
            selectedPaletteId = StaticPaletteIds.HALLOWEEN
        )

        val expected = baseOptions.filterNot { it == StaticPaletteIds.CHRISTMAS }

        assertEquals(expected, filtered)
    }

    @Test
    fun `christmas palette stays visible during the christmas season`() {
        val filtered = filterSeasonalStaticPalettes(
            baseOptions = baseOptions,
            isChristmasSeason = true,
            isHalloweenSeason = false,
            selectedPaletteId = StaticPaletteIds.DEFAULT
        )

        val expected = baseOptions.filterNot { it == StaticPaletteIds.HALLOWEEN }

        assertEquals(expected, filtered)
    }
}
