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
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Guards the static palettes against the contrast failures they used to ship.
 *
 * Checking only each `on*` role against its own container is not enough: components also draw
 * `primary` as text on the surface (text buttons, links, selected icons), `onSurfaceVariant` as
 * secondary text on raised containers, and `inversePrimary` as the action on a snackbar. The
 * brand-bright palettes passed the narrow check and still rendered unreadable buttons.
 */
class StaticPaletteContrastTest {

    private val palettes: List<String> =
        StaticPaletteIds.withDefault.filterNot { it == StaticPaletteIds.DEFAULT }

    @Test
    fun `all static palettes keep readable text on every surface it is drawn on`() {
        val failures = schemes().flatMap { (name, scheme) ->
            textPairs(scheme).mapNotNull { (role, colors) ->
                val ratio = contrast(colors.first, colors.second)
                if (ratio < TEXT_CONTRAST) "$name $role ${"%.2f".format(ratio)}" else null
            }
        }

        assertTrue(failures.isEmpty(), "Text contrast below $TEXT_CONTRAST:1: $failures")
    }

    @Test
    fun `all static palettes keep outlines visible against surfaces`() {
        val failures = schemes().flatMap { (name, scheme) ->
            listOf(
                "outline/surface" to contrast(scheme.outline, scheme.surface),
                "outline/surfaceContainerHighest" to
                    contrast(scheme.outline, scheme.surfaceContainerHighest),
            ).mapNotNull { (role, ratio) ->
                if (ratio < NON_TEXT_CONTRAST) "$name $role ${"%.2f".format(ratio)}" else null
            }
        }

        assertTrue(failures.isEmpty(), "Outline contrast below $NON_TEXT_CONTRAST:1: $failures")
    }

    @Test
    fun `static palettes define their own fixed roles`() {
        // lightColorScheme() and darkColorScheme() fill any fixed role left out with Material's
        // baseline purple, so a palette that forgets them tints fixed-role components purple.
        val baseline = lightColorScheme()
        val failures = schemes().mapNotNull { (name, scheme) ->
            val borrowed = listOf(
                "primaryFixed" to (scheme.primaryFixed to baseline.primaryFixed),
                "secondaryFixed" to (scheme.secondaryFixed to baseline.secondaryFixed),
                "tertiaryFixed" to (scheme.tertiaryFixed to baseline.tertiaryFixed),
            ).filter { (_, colors) -> colors.first == colors.second }.map { it.first }
            if (borrowed.isEmpty() || name.startsWith(StaticPaletteIds.PURPLE)) null
            else "$name $borrowed"
        }

        assertTrue(failures.isEmpty(), "Fixed roles still use the baseline scheme: $failures")
    }

    private fun schemes(): List<Pair<String, ColorScheme>> = palettes.flatMap { id ->
        val palette = ThemePaletteProvider.paletteById(id)
        listOf("$id light" to palette.lightColorScheme, "$id dark" to palette.darkColorScheme)
    }

    private fun textPairs(scheme: ColorScheme): List<Pair<String, Pair<Color, Color>>> = listOf(
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
        "inversePrimary/inverseSurface" to (scheme.inversePrimary to scheme.inverseSurface),
        "onSurface/surfaceContainerHighest" to
            (scheme.onSurface to scheme.surfaceContainerHighest),
        "onSurfaceVariant/surfaceContainerHighest" to
            (scheme.onSurfaceVariant to scheme.surfaceContainerHighest),
        "primary/surface" to (scheme.primary to scheme.surface),
        "primary/surfaceContainer" to (scheme.primary to scheme.surfaceContainer),
        "primary/surfaceContainerHighest" to (scheme.primary to scheme.surfaceContainerHighest),
        "secondary/surface" to (scheme.secondary to scheme.surface),
        "tertiary/surface" to (scheme.tertiary to scheme.surface),
        "error/surface" to (scheme.error to scheme.surface),
        "onPrimaryFixed/primaryFixed" to (scheme.onPrimaryFixed to scheme.primaryFixed),
        "onPrimaryFixedVariant/primaryFixed" to
            (scheme.onPrimaryFixedVariant to scheme.primaryFixed),
        "onSecondaryFixed/secondaryFixed" to (scheme.onSecondaryFixed to scheme.secondaryFixed),
        "onTertiaryFixed/tertiaryFixed" to (scheme.onTertiaryFixed to scheme.tertiaryFixed),
    )

    private fun contrast(foreground: Color, background: Color): Float {
        val lighter = maxOf(foreground.luminance(), background.luminance())
        val darker = minOf(foreground.luminance(), background.luminance())
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    private companion object {
        const val TEXT_CONTRAST: Float = 4.5f
        const val NON_TEXT_CONTRAST: Float = 3f
    }
}
