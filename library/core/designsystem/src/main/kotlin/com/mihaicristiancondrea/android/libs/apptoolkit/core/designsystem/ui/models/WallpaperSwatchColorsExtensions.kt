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

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

/**
 * The colors that make this scheme recognizable, for a palette swatch.
 *
 * Each accent shows whichever of its role and its container is more colorful. A scheme's
 * `secondary` is deliberately muted, and in a light scheme a bright brand color can sit in the
 * container while the role itself is a darker tone that reads as text. Showing the roles as they
 * were made swatches look duller than the palettes they stand for.
 */
fun ColorScheme.toSwatchColors(): WallpaperSwatchColors = WallpaperSwatchColors(
    primary = moreColorful(primary, primaryContainer),
    secondary = moreColorful(secondary, secondaryContainer),
    tertiary = moreColorful(tertiary, tertiaryContainer),
)

private fun moreColorful(role: Color, container: Color): Color =
    if (colorfulness(container) > colorfulness(role)) container else role

/** The spread between the strongest and weakest channel: zero for grays, largest for pure hues. */
private fun colorfulness(color: Color): Float =
    max(color.red, max(color.green, color.blue)) - min(color.red, min(color.green, color.blue))
