package com.d4rk.android.libs.apptoolkit.app.theme.style

import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.rose.rosePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.StaticPaletteIds

object ThemePaletteProvider {
    var defaultPalette: ColorPalette = bluePalette

    fun paletteById(id: String): ColorPalette = when (id) {
        StaticPaletteIds.MONOCHROME -> monochromePalette
        StaticPaletteIds.BLUE -> bluePalette
        StaticPaletteIds.GREEN -> greenPalette
        StaticPaletteIds.RED -> redPalette
        StaticPaletteIds.YELLOW -> yellowPalette
        StaticPaletteIds.ROSE -> rosePalette
        StaticPaletteIds.DEFAULT -> defaultPalette
        else -> defaultPalette
    }
}
