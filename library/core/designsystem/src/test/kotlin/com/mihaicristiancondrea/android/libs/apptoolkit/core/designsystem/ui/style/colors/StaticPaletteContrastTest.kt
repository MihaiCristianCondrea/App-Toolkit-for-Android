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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StaticPaletteContrastTest {

    @Test
    fun `all static palettes keep readable foreground and background pairs`() {
        for (id in StaticPaletteIds.withDefault.filterNot { it == StaticPaletteIds.DEFAULT }) {
            val palette = ThemePaletteProvider.paletteById(id)
            for ((mode, scheme) in listOf(
                "light" to palette.lightColorScheme,
                "dark" to palette.darkColorScheme,
            )) {
                for ((role, colors) in foregroundPairs(scheme)) {
                    val (foreground, background) = colors
                    val contrast = (maxOf(foreground.luminance(), background.luminance()) + 0.05f) /
                        (minOf(foreground.luminance(), background.luminance()) + 0.05f)
                    assertTrue(contrast >= 4.5f, "$id $mode $role contrast is $contrast")
                }
            }
        }
    }

    private fun foregroundPairs(scheme: ColorScheme): List<Pair<String, Pair<Color, Color>>> = listOf(
        "primary" to (scheme.onPrimary to scheme.primary),
        "primaryContainer" to (scheme.onPrimaryContainer to scheme.primaryContainer),
        "secondary" to (scheme.onSecondary to scheme.secondary),
        "secondaryContainer" to (scheme.onSecondaryContainer to scheme.secondaryContainer),
        "tertiary" to (scheme.onTertiary to scheme.tertiary),
        "tertiaryContainer" to (scheme.onTertiaryContainer to scheme.tertiaryContainer),
        "error" to (scheme.onError to scheme.error),
        "errorContainer" to (scheme.onErrorContainer to scheme.errorContainer),
        "background" to (scheme.onBackground to scheme.background),
        "surface" to (scheme.onSurface to scheme.surface),
        "surfaceVariant" to (scheme.onSurfaceVariant to scheme.surfaceVariant),
        "inverseSurface" to (scheme.inverseOnSurface to scheme.inverseSurface),
    )
}
