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
