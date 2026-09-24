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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.views

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.toSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MaterialYouCircleSwatchTest {

    @Test
    fun `the selection check stands out on every static palette swatch`() {
        val failures = StaticPaletteIds.withDefault.flatMap { id ->
            val palette = ThemePaletteProvider.paletteById(id)
            listOf("light" to palette.lightColorScheme, "dark" to palette.darkColorScheme)
                .mapNotNull { (mode, scheme) ->
                    val primary = scheme.toSwatchColors().primary
                    val ratio = contrast(selectionBadgeColor(primary), primary)
                    if (ratio < NON_TEXT_CONTRAST) "$id $mode ${"%.2f".format(ratio)}" else null
                }
        }

        assertTrue(failures.isEmpty(), "Check contrast below $NON_TEXT_CONTRAST:1: $failures")
    }

    @Test
    fun `the check stands out on the extremes`() {
        listOf(Color.Black, Color.White, Color(0xFF808080), Color(0xFF3DDC84)).forEach { primary ->
            val ratio = contrast(selectionBadgeColor(primary), primary)
            assertTrue(ratio >= NON_TEXT_CONTRAST, "$primary: $ratio")
        }
    }

    private fun contrast(first: Color, second: Color): Float {
        val lighter = maxOf(first.luminance(), second.luminance())
        val darker = minOf(first.luminance(), second.luminance())
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    private companion object {
        const val NON_TEXT_CONTRAST: Float = 3f
    }
}
