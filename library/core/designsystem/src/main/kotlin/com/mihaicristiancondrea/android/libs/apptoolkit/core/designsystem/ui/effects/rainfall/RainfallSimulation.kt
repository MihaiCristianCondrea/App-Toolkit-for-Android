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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * The moving state of a rainfall, kept apart from Compose so the motion can be tested directly.
 *
 * Drops live in parallel primitive arrays, as the snowfall's flakes do: every frame touches every
 * drop, and the arrays keep that loop free of allocation and of snapshot-state writes.
 *
 * All positions are in pixels. [resize] must be called before [advance] or [draw] do anything.
 */
internal class RainfallSimulation(
    private val style: RainfallStyle,
    private val random: Random = Random.Default,
) {
    private var width: Float = 0f
    private var height: Float = 0f
    private var thicknessPx: Float = 0f

    private var x = FloatArray(0)
    private var y = FloatArray(0)
    private var length = FloatArray(0)

    /** Pixels per millisecond, straight down. */
    private var fallSpeed = FloatArray(0)

    /** Each drop's final color, its opacity already applied. */
    private var color = Array(0) { Color.Unspecified }

    /** Sideways pixels per pixel fallen: the slant the wind gives every streak. */
    private var slant: Float = 0f

    /** Number of drops currently simulated. */
    val dropCount: Int
        get() = y.size

    /** The head of drop [index], the end nearest the ground. */
    fun positionOf(index: Int): Offset = Offset(x[index], y[index])

    /**
     * Rebuilds the drops for a [widthPx] by [heightPx] surface, spread over its whole height, so
     * the first frame already looks like rain that has been falling.
     *
     * @param pxPerDp Screen density, so lengths and speeds are the same physical size everywhere.
     */
    fun resize(widthPx: Int, heightPx: Int, pxPerDp: Float) {
        if (widthPx == width.toInt() && heightPx == height.toInt() && dropCount > 0) return
        width = widthPx.toFloat()
        height = heightPx.toFloat()
        thicknessPx = style.thickness.value * pxPerDp
        slant = style.wind.coerceIn(-1f, 1f) * MAX_SLANT

        val count = dropCountFor(widthPx = widthPx, heightPx = heightPx, pxPerDp = pxPerDp)
        x = FloatArray(count)
        y = FloatArray(count)
        length = FloatArray(count)
        fallSpeed = FloatArray(count)
        color = Array(count) { Color.Unspecified }

        val minLength = style.minLength.value * pxPerDp
        val maxLength = style.maxLength.value * pxPerDp
        val speed = style.speed.coerceAtLeast(0f)
        for (index in 0 until count) {
            // Length decides depth: longer drops are "closer", so they fall faster and show more.
            val depth = random.nextFloat()
            length[index] = minLength + (maxLength - minLength) * depth
            fallSpeed[index] = (MIN_FALL_DP_PER_SECOND + FALL_RANGE_DP_PER_SECOND * depth) *
                speed * pxPerDp / MILLIS_PER_SECOND
            val opacity = style.minAlpha + (style.maxAlpha - style.minAlpha) * depth
            val base = style.colors[random.nextInt(style.colors.size)]
            color[index] = base.copy(alpha = base.alpha * opacity)
            x[index] = random.nextFloat() * width
            y[index] = random.nextFloat() * height
        }
    }

    /** Moves every drop on by [elapsedMillis], bringing those that left the surface back above it. */
    fun advance(elapsedMillis: Float) {
        val step = elapsedMillis.coerceIn(0f, MAX_STEP_MILLIS)
        if (step == 0f) return
        for (index in 0 until dropCount) {
            val fall = fallSpeed[index] * step
            y[index] += fall
            x[index] += fall * slant
            if (y[index] - length[index] > height) {
                y[index] = -random.nextFloat() * height * RESPAWN_SPREAD
                x[index] = random.nextFloat() * width
            }
            // Drops blown off one side come back in on the other.
            if (x[index] < 0f) x[index] += width else if (x[index] > width) x[index] -= width
        }
    }

    /** Draws every drop as a streak trailing up and against the wind from its head. */
    fun draw(scope: DrawScope) {
        for (index in 0 until dropCount) {
            val head = positionOf(index)
            val tail = Offset(head.x - length[index] * slant, head.y - length[index])
            scope.drawLine(
                color = color[index],
                start = tail,
                end = head,
                strokeWidth = thicknessPx,
                cap = StrokeCap.Round,
            )
        }
    }

    private fun dropCountFor(widthPx: Int, heightPx: Int, pxPerDp: Float): Int {
        if (widthPx <= 0 || heightPx <= 0 || pxPerDp <= 0f) return 0
        val areaDp = (widthPx / pxPerDp) * (heightPx / pxPerDp)
        val wanted = (areaDp * style.density.coerceIn(0f, 1f) * DROPS_PER_SQUARE_DP).roundToInt()
        return min(wanted, style.maxDrops)
    }

    private companion object {
        const val DROPS_PER_SQUARE_DP: Float = 0.0006f
        const val MIN_FALL_DP_PER_SECOND: Float = 420f
        const val FALL_RANGE_DP_PER_SECOND: Float = 360f
        const val MAX_SLANT: Float = 0.35f
        const val RESPAWN_SPREAD: Float = 0.2f
        const val MAX_STEP_MILLIS: Float = 50f
        const val MILLIS_PER_SECOND: Float = 1000f
    }
}
