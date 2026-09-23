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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * The moving state of a snowfall, kept apart from Compose so the motion can be tested directly.
 *
 * Flakes live in parallel primitive arrays rather than one object each. Every frame touches every
 * flake, and plain arrays keep that loop free of allocation and of snapshot-state writes: the
 * modifier that owns this invalidates the draw phase once per frame instead of every flake
 * invalidating its own state.
 *
 * All positions are in pixels. [resize] must be called before [advance] or [draw] do anything.
 */
internal class SnowfallSimulation(
    private val style: SnowfallStyle,
    private val random: Random = Random.Default,
) {
    private var width: Float = 0f
    private var height: Float = 0f

    /** Horizontal position the flake sways around. */
    private var baseX = FloatArray(0)
    private var y = FloatArray(0)
    private var radius = FloatArray(0)

    /** Pixels per millisecond. */
    private var fallSpeed = FloatArray(0)
    private var swayAmplitude = FloatArray(0)
    private var swayPhase = FloatArray(0)

    /** Radians per millisecond. */
    private var swayFrequency = FloatArray(0)
    private var rotation = FloatArray(0)
    private var rotationSpeed = FloatArray(0)

    /** Each flake's final color, its opacity already applied, so drawing does no color math. */
    private var color = Array(0) { Color.Unspecified }
    private var crystal = BooleanArray(0)

    /**
     * A six-armed crystal with a pair of branches on each arm, in a unit-radius space.
     *
     * Built on first draw rather than up front: a graphics path needs the Android runtime, and the
     * motion itself is tested on the plain JVM.
     */
    private val crystalPath: Path by lazy(LazyThreadSafetyMode.NONE) {
        Path().apply {
            repeat(ARMS) { arm ->
                val angle = arm * (TWO_PI / ARMS)
                val dx = cos(angle)
                val dy = sin(angle)
                moveTo(0f, 0f)
                lineTo(dx, dy)
                val branchX = dx * BRANCH_POSITION
                val branchY = dy * BRANCH_POSITION
                for (side in SIDES) {
                    val branchAngle = angle + side * BRANCH_ANGLE
                    moveTo(branchX, branchY)
                    lineTo(
                        branchX + cos(branchAngle) * BRANCH_LENGTH,
                        branchY + sin(branchAngle) * BRANCH_LENGTH,
                    )
                }
            }
        }
    }

    private val crystalStroke = Stroke(width = CRYSTAL_STROKE, cap = StrokeCap.Round)

    private var windPxPerMs: Float = 0f

    /** Number of flakes currently simulated. */
    val flakeCount: Int
        get() = y.size

    /** Current position of flake [index], including its sway. */
    fun positionOf(index: Int): Offset =
        Offset(baseX[index] + sin(swayPhase[index]) * swayAmplitude[index], y[index])

    /** Radius of flake [index] in pixels. */
    fun radiusOf(index: Int): Float = radius[index]

    /**
     * Rebuilds the flakes for a [widthPx] by [heightPx] surface.
     *
     * Flakes start spread over the whole height, so the first frame already looks like snow that has
     * been falling, rather than a curtain dropping in from the top.
     *
     * @param pxPerDp Screen density, so sizes and speeds are the same physical size on every device.
     */
    fun resize(widthPx: Int, heightPx: Int, pxPerDp: Float) {
        if (widthPx == width.toInt() && heightPx == height.toInt() && flakeCount > 0) return
        width = widthPx.toFloat()
        height = heightPx.toFloat()

        val count = flakeCountFor(widthPx = widthPx, heightPx = heightPx, pxPerDp = pxPerDp)
        baseX = FloatArray(count)
        y = FloatArray(count)
        radius = FloatArray(count)
        fallSpeed = FloatArray(count)
        swayAmplitude = FloatArray(count)
        swayPhase = FloatArray(count)
        swayFrequency = FloatArray(count)
        rotation = FloatArray(count)
        rotationSpeed = FloatArray(count)
        color = Array(count) { Color.Unspecified }
        crystal = BooleanArray(count)

        val minRadius = style.minSize.value * pxPerDp / 2f
        val maxRadius = style.maxSize.value * pxPerDp / 2f
        val speed = style.speed.coerceAtLeast(0f)
        windPxPerMs = style.wind.coerceIn(-1f, 1f) * WIND_DP_PER_SECOND * pxPerDp / MILLIS_PER_SECOND

        for (index in 0 until count) {
            // Size decides depth: bigger flakes are "closer", so they fall faster and are more opaque.
            val depth = random.nextFloat()
            radius[index] = minRadius + (maxRadius - minRadius) * depth
            fallSpeed[index] = (MIN_FALL_DP_PER_SECOND + FALL_RANGE_DP_PER_SECOND * depth) *
                speed * pxPerDp / MILLIS_PER_SECOND
            val opacity = style.minAlpha + (style.maxAlpha - style.minAlpha) * depth
            swayAmplitude[index] = (MIN_SWAY_DP + random.nextFloat() * SWAY_RANGE_DP) * pxPerDp
            swayPhase[index] = random.nextFloat() * TWO_PI
            swayFrequency[index] = (MIN_SWAY_HZ + random.nextFloat() * SWAY_RANGE_HZ) * TWO_PI /
                MILLIS_PER_SECOND
            rotation[index] = random.nextFloat() * FULL_TURN_DEGREES
            rotationSpeed[index] = (random.nextFloat() - 0.5f) * MAX_ROTATION_DEGREES_PER_MS
            val base = style.colors[random.nextInt(style.colors.size)]
            color[index] = base.copy(alpha = base.alpha * opacity)
            crystal[index] = when (style.shape) {
                SnowflakeShape.Dots -> false
                SnowflakeShape.Crystals -> true
                SnowflakeShape.Mixed -> depth > MIXED_CRYSTAL_DEPTH && random.nextBoolean()
            }
            baseX[index] = random.nextFloat() * width
            y[index] = random.nextFloat() * height
        }
    }

    /**
     * Moves every flake forward by [elapsedMillis].
     *
     * The step is clamped, so a frame that arrives late (after a pause, or a slow first frame) moves
     * the snow by one ordinary step instead of teleporting it.
     */
    fun advance(elapsedMillis: Float) {
        val step = elapsedMillis.coerceIn(0f, MAX_STEP_MILLIS)
        if (step == 0f) return
        for (index in 0 until flakeCount) {
            y[index] += fallSpeed[index] * step
            baseX[index] += windPxPerMs * step
            swayPhase[index] = (swayPhase[index] + swayFrequency[index] * step) % TWO_PI
            rotation[index] = (rotation[index] + rotationSpeed[index] * step) % FULL_TURN_DEGREES

            val margin = radius[index] + swayAmplitude[index]
            if (y[index] - radius[index] > height) {
                // Re-enter above the top edge at a new column, so the pattern never visibly repeats.
                y[index] = -radius[index]
                baseX[index] = random.nextFloat() * width
            }
            if (baseX[index] < -margin) baseX[index] += width + 2 * margin
            if (baseX[index] > width + margin) baseX[index] -= width + 2 * margin
        }
    }

    /** Draws every flake into this scope. */
    fun draw(scope: DrawScope) {
        for (index in 0 until flakeCount) {
            val color = color[index]
            val center = positionOf(index)
            if (crystal[index]) {
                scope.translate(left = center.x, top = center.y) {
                    rotate(degrees = rotation[index], pivot = Offset.Zero) {
                        scale(scale = radius[index], pivot = Offset.Zero) {
                            drawPath(
                                path = crystalPath,
                                color = color,
                                style = crystalStroke,
                            )
                        }
                    }
                }
            } else {
                scope.drawCircle(color = color, radius = radius[index], center = center)
            }
        }
    }

    private fun flakeCountFor(widthPx: Int, heightPx: Int, pxPerDp: Float): Int {
        if (widthPx <= 0 || heightPx <= 0 || pxPerDp <= 0f) return 0
        val areaDp = (widthPx / pxPerDp) * (heightPx / pxPerDp)
        val wanted = (areaDp * style.density.coerceIn(0f, 1f) * FLAKES_PER_SQUARE_DP).roundToInt()
        return wanted.coerceAtMost(style.maxFlakes)
    }

    private companion object {
        /** About 70 flakes on a phone-sized screen at the default density. */
        const val FLAKES_PER_SQUARE_DP: Float = 0.00055f
        const val MIN_FALL_DP_PER_SECOND: Float = 28f
        const val FALL_RANGE_DP_PER_SECOND: Float = 52f
        const val WIND_DP_PER_SECOND: Float = 40f
        const val MIN_SWAY_DP: Float = 4f
        const val SWAY_RANGE_DP: Float = 14f
        const val MIN_SWAY_HZ: Float = 0.05f
        const val SWAY_RANGE_HZ: Float = 0.15f
        const val MAX_ROTATION_DEGREES_PER_MS: Float = 0.06f
        const val MIXED_CRYSTAL_DEPTH: Float = 0.7f
        const val MAX_STEP_MILLIS: Float = 50f
        const val MILLIS_PER_SECOND: Float = 1000f
        const val FULL_TURN_DEGREES: Float = 360f
        const val TWO_PI: Float = (2 * PI).toFloat()

        /** Stroke width in unit space, where an arm is one unit long. */
        const val CRYSTAL_STROKE: Float = 0.2f

        const val ARMS: Int = 6
        const val BRANCH_POSITION: Float = 0.55f
        const val BRANCH_LENGTH: Float = 0.35f
        const val BRANCH_ANGLE: Float = (PI / 4).toFloat()
        val SIDES: FloatArray = floatArrayOf(-1f, 1f)
    }
}
