package com.d4rk.android.libs.apptoolkit.app.theme.ui.style

import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.android.androidPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.christmas.christmasPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.rose.rosePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds

object ThemePaletteProvider {
    var defaultPalette: ColorPalette = bluePalette

    fun paletteById(id: String): ColorPalette = when (id) {
        StaticPaletteIds.MONOCHROME -> monochromePalette
        StaticPaletteIds.GOOGLE_BLUE -> bluePalette
        StaticPaletteIds.ANDROID -> androidPalette
        StaticPaletteIds.GREEN -> greenPalette
        StaticPaletteIds.RED -> redPalette
        StaticPaletteIds.YELLOW -> yellowPalette
        StaticPaletteIds.ROSE -> rosePalette
        StaticPaletteIds.CHRISTMAS -> christmasPalette
        StaticPaletteIds.DEFAULT -> defaultPalette
        else -> defaultPalette
    }
}
