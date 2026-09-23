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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WallpaperSwatchColorsTest {

    @Test
    fun `a light scheme shows its brand color rather than the darker text tone`() {
        val android = ThemePaletteProvider.paletteById(StaticPaletteIds.ANDROID).lightColorScheme

        val swatch = android.toSwatchColors()

        assertEquals(android.primaryContainer, swatch.primary, "the exact Android green")
    }

    @Test
    fun `a muted role gives way to its more colorful container and back`() {
        val blue = ThemePaletteProvider.paletteById(StaticPaletteIds.GOOGLE_BLUE).lightColorScheme

        val swatch = blue.toSwatchColors()

        // Blue's light primary is already its most saturated tone; its pale container is not.
        assertEquals(blue.primary, swatch.primary)
    }

    @Test
    fun `grays stay gray`() {
        val mono = ThemePaletteProvider.paletteById(StaticPaletteIds.MONOCHROME).darkColorScheme

        assertEquals(mono.primary, mono.toSwatchColors().primary)
    }
}
