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
