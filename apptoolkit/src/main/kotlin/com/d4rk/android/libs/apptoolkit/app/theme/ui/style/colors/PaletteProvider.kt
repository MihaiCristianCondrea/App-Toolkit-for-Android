package com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors

import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.android.androidPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.rose.rosePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.christmas.christmasPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.halloween.halloweenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.skin.skinPalette
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
        StaticPaletteIds.SKIN -> skinPalette
        StaticPaletteIds.CHRISTMAS -> christmasPalette
        StaticPaletteIds.HALLOWEEN -> halloweenPalette
        StaticPaletteIds.DEFAULT -> defaultPalette
        else -> defaultPalette
    }
}
