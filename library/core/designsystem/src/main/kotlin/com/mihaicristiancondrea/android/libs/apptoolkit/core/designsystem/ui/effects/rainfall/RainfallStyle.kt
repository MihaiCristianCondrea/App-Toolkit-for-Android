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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.rainfall

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Appearance and motion of [rainfall].
 *
 * @property density How much rain falls, from `0` (none) to `1` (a downpour). The drop count
 * scales with the drawn area, so a phone and a tablet at the same density look equally wet.
 * @property colors Drop colors, picked at random per drop. Choose colors that stand apart from the
 * surface behind them.
 * @property minLength Shortest streak.
 * @property maxLength Longest streak. Longer streaks also fall faster, so length doubles as depth.
 * @property thickness Stroke width of a streak at middle depth. Near streaks are drawn thicker, far
 * ones thinner.
 * @property speed Fall speed multiplier. `1` is steady rain.
 * @property wind Slant and drift from `-1` (blown to the left) to `1` (to the right).
 * @property gusts How much the wind swings around [wind], from `0` (steady) to `1` (stormy).
 * @property showers How much the rain comes and goes, from `0` (steady) to `1` (dropping to
 * nothing between showers).
 * @property splashes Whether landing drops leave a widening ring.
 * @property splashSize How wide the splash of a drop at middle depth grows.
 * @property minAlpha Opacity of the faintest drops.
 * @property maxAlpha Opacity of the most visible drops.
 * @property maxDrops Hard cap on the drop count, which bounds the per-frame cost.
 */
@Immutable
data class RainfallStyle(
    val density: Float = 0.4f,
    val colors: List<Color> = listOf(Color(0xFFB3C7DD)),
    val minLength: Dp = 10.dp,
    val maxLength: Dp = 22.dp,
    val thickness: Dp = 1.5.dp,
    val speed: Float = 1f,
    val wind: Float = 0.15f,
    val gusts: Float = 0.5f,
    val showers: Float = 0.35f,
    val splashes: Boolean = true,
    val splashSize: Dp = 7.dp,
    val minAlpha: Float = 0.25f,
    val maxAlpha: Float = 0.6f,
    val maxDrops: Int = 180,
) {
    init {
        require(colors.isNotEmpty()) { "RainfallStyle needs at least one color" }
        require(minLength <= maxLength) { "minLength must not exceed maxLength" }
        require(minAlpha <= maxAlpha) { "minAlpha must not exceed maxAlpha" }
        require(maxDrops >= 0) { "maxDrops must not be negative" }
        require(gusts in 0f..1f) { "gusts must be between 0 and 1" }
        require(showers in 0f..1f) { "showers must be between 0 and 1" }
    }
}
