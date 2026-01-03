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
