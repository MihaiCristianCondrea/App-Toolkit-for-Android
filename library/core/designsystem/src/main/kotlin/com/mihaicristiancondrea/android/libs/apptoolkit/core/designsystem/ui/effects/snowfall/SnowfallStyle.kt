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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.snowfall

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** How individual flakes are drawn. */
enum class SnowflakeShape {
    /** Soft round dots, the lightest option to draw. */
    Dots,

    /** Six-armed crystals that slowly turn as they fall. */
    Crystals,

    /** Mostly dots with an occasional larger crystal, which reads as depth. */
    Mixed,
}

/**
 * Appearance and motion of [snowfall].
 *
 * @property density How much snow falls, from `0` (none) to `1` (a blizzard). The flake count
 * scales with the drawn area, so a phone and a tablet at the same density look equally snowy.
 * @property colors Flake colors, picked at random per flake. Choose colors that stand apart from
 * the surface behind them: white flakes disappear on a light theme.
 * @property minSize Smallest flake diameter.
 * @property maxSize Largest flake diameter. Larger flakes also fall faster, so size doubles as depth.
 * @property speed Fall speed multiplier. `1` is a gentle drift; values above `2` look like a storm.
 * @property wind Constant horizontal drift from `-1` (strong wind to the left) to `1` (to the right).
 * @property minAlpha Opacity of the faintest flakes.
 * @property maxAlpha Opacity of the most visible flakes.
 * @property shape How the flakes are drawn.
 * @property maxFlakes Hard cap on the flake count, which bounds the per-frame cost on large or
 * very dense surfaces.
 */
@Immutable
data class SnowfallStyle(
    val density: Float = 0.35f,
    val colors: List<Color> = listOf(Color.White),
    val minSize: Dp = 2.dp,
    val maxSize: Dp = 7.dp,
    val speed: Float = 1f,
    val wind: Float = 0f,
    val minAlpha: Float = 0.45f,
    val maxAlpha: Float = 0.95f,
    val shape: SnowflakeShape = SnowflakeShape.Mixed,
    val maxFlakes: Int = 160,
) {
    init {
        require(colors.isNotEmpty()) { "SnowfallStyle needs at least one color" }
        require(minSize <= maxSize) { "minSize must not exceed maxSize" }
        require(minAlpha <= maxAlpha) { "minAlpha must not exceed maxAlpha" }
        require(maxFlakes >= 0) { "maxFlakes must not be negative" }
    }
}
