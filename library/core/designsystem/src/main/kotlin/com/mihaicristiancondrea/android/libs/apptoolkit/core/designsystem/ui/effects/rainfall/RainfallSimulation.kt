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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.ParticleSimulation
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * The moving state of a rainfall, kept apart from Compose so the motion can be tested directly.
 *
 * Drops live in parallel primitive arrays, as the snowfall's flakes do: every frame touches every
 * drop, and the arrays keep that loop free of allocation and of snapshot-state writes. Splashes
 * live in a fixed pool of the same kind, reused in turn.
 *
 * What keeps the rain from looking like a screensaver:
 * - Depth: each drop has one, and a nearer drop is longer, thicker, brighter and faster, and lands
 *   lower on the surface, as if the surface were a floor seen from above.
 * - Splashes: a drop that lands leaves a flat ring that widens and fades.
 * - Gusts: the wind swings around its [RainfallStyle.wind] on two slow, unrelated waves, so every
 *   streak leans and drifts together, more one moment and less the next.
 * - Showers: the share of drops that fall visibly swells and eases on another slow wave. A drop only
 *   decides when it starts falling, so the rain thickens and thins without drops popping in or out.
 *
 * All positions are in pixels. [resize] must be called before [advance] or [draw] do anything.
 */
internal class RainfallSimulation(
    private val style: RainfallStyle,
    private val random: Random = Random.Default,
) : ParticleSimulation {
    private var width: Float = 0f
    private var height: Float = 0f
    private var pxPerDp: Float = 1f

    private var x = FloatArray(0)
    private var y = FloatArray(0)
    private var length = FloatArray(0)
    private var thickness = FloatArray(0)

    /** Pixels per millisecond, straight down. */
    private var fallSpeed = FloatArray(0)

    /** How far down the surface the drop lands; below the surface, it falls out of sight. */
    private var landingY = FloatArray(0)

    /** How wide the drop's splash grows, in pixels. */
    private var splashRadius = FloatArray(0)

    /** Whether the drop is seen on this fall; see [intensity]. */
    private var visible = BooleanArray(0)

    /** Each drop's final color, its opacity already applied. */
    private var color = Array(0) { Color.Unspecified }

    private var splashX = FloatArray(0)
    private var splashY = FloatArray(0)
    private var splashMaxRadius = FloatArray(0)

    /** Milliseconds since the splash started, or below zero while its slot is free. */
    private var splashAge = FloatArray(0)
    private var splashColor = Array(0) { Color.Unspecified }
    private var nextSplash: Int = 0
    private var splashStroke: Stroke = Stroke()

    /**
     * Where each of the gusts' and the showers' two waves is in its cycle, as a share of a turn.
     * Each wraps at its own full cycle, so the waves stay smooth however long the rain falls.
     */
    private val gustPhases = FloatArray(2)
    private val showerPhases = FloatArray(2)

    /** Sideways pixels per pixel fallen, gusts included: the lean every streak shares right now. */
    var windSlant: Float = baseSlant()
        private set

    /** The share of drops that fall visibly right now, from `1 - showers` to `1`. */
    var intensity: Float = 1f
        private set

    val dropCount: Int
        get() = y.size

    override val particleCount: Int
        get() = dropCount

    /** Number of splashes currently spreading. */
    val splashCount: Int
        get() = splashAge.count { it >= 0f }

    /** The head of drop [index], the end nearest the ground. */
    fun positionOf(index: Int): Offset = Offset(x[index], y[index])

    /**
     * Rebuilds the drops for a [widthPx] by [heightPx] surface, spread over its whole height, so
     * the first frame already looks like rain that has been falling.
     *
     * @param pxPerDp Screen density, so lengths and speeds are the same physical size everywhere.
     */
    override fun resize(widthPx: Int, heightPx: Int, pxPerDp: Float) {
        if (widthPx == width.toInt() && heightPx == height.toInt() && dropCount > 0) return
        width = widthPx.toFloat()
        height = heightPx.toFloat()
        this.pxPerDp = pxPerDp

        val count = dropCountFor(widthPx = widthPx, heightPx = heightPx, pxPerDp = pxPerDp)
        x = FloatArray(count)
        y = FloatArray(count)
        length = FloatArray(count)
        thickness = FloatArray(count)
        fallSpeed = FloatArray(count)
        landingY = FloatArray(count)
        splashRadius = FloatArray(count)
        visible = BooleanArray(count)
        color = Array(count) { Color.Unspecified }

        val minLength = style.minLength.value * pxPerDp
        val maxLength = style.maxLength.value * pxPerDp
        val speed = style.speed.coerceAtLeast(0f)
        for (index in 0 until count) {
            val depth = random.nextFloat()
            length[index] = minLength + (maxLength - minLength) * depth
            thickness[index] = style.thickness.value * pxPerDp * (MIN_THICKNESS + THICKNESS_RANGE * depth)
            fallSpeed[index] = (MIN_FALL_DP_PER_SECOND + FALL_RANGE_DP_PER_SECOND * depth) *
                speed * pxPerDp / MILLIS_PER_SECOND
            splashRadius[index] = style.splashSize.value * pxPerDp * (MIN_SPLASH + SPLASH_RANGE * depth)
            val opacity = style.minAlpha + (style.maxAlpha - style.minAlpha) * depth
            val base = style.colors[random.nextInt(style.colors.size)]
            color[index] = base.copy(alpha = base.alpha * opacity)
            x[index] = random.nextFloat() * width
            landingY[index] = landingFor(depth)
            // Anywhere above where it lands, so the first frames are not one burst of splashes.
            y[index] = random.nextFloat() * min(landingY[index], height)
            visible[index] = true
        }

        val splashes = if (style.splashes) min(count, MAX_SPLASHES) else 0
        splashX = FloatArray(splashes)
        splashY = FloatArray(splashes)
        splashMaxRadius = FloatArray(splashes)
        splashAge = FloatArray(splashes) { FREE }
        splashColor = Array(splashes) { Color.Unspecified }
        nextSplash = 0
        splashStroke = Stroke(width = style.thickness.value * pxPerDp * SPLASH_STROKE)
    }

    /**
     * Moves every drop on by [elapsedMillis], splashing those that land and bringing them back
     * above the surface, and moves the wind, the showers and the splashes on with them.
     */
    override fun advance(elapsedMillis: Float) {
        val step = elapsedMillis.coerceIn(0f, MAX_STEP_MILLIS)
        if (step == 0f) return
        advancePhases(gustPhases, GUST_PERIODS, step)
        advancePhases(showerPhases, SHOWER_PERIODS, step)
        windSlant = baseSlant() + style.gusts * MAX_GUST_SLANT * wave(gustPhases)
        intensity = 1f - style.showers * (HALF + HALF * wave(showerPhases))

        for (index in splashAge.indices) {
            if (splashAge[index] < 0f) continue
            splashAge[index] += step
            if (splashAge[index] >= SPLASH_MILLIS) splashAge[index] = FREE
        }

        for (index in 0 until dropCount) {
            val fall = fallSpeed[index] * step
            y[index] += fall
            x[index] += fall * windSlant
            if (x[index] < 0f) x[index] += width else if (x[index] > width) x[index] -= width
            if (y[index] >= landingY[index]) land(index)
        }
    }

    /** Draws every visible drop as a streak trailing up and against the wind, then the splashes. */
    override fun draw(scope: DrawScope) {
        for (index in 0 until dropCount) {
            if (!visible[index]) continue
            val head = positionOf(index)
            val tail = Offset(head.x - length[index] * windSlant, head.y - length[index])
            scope.drawLine(
                color = color[index],
                start = tail,
                end = head,
                strokeWidth = thickness[index],
                cap = StrokeCap.Round,
            )
        }
        for (index in splashAge.indices) {
            val age = splashAge[index]
            if (age < 0f) continue
            val progress = age / SPLASH_MILLIS
            val eased = 1f - (1f - progress) * (1f - progress)
            val radius = splashMaxRadius[index] * (MIN_SPLASH_START + (1f - MIN_SPLASH_START) * eased)
            val flat = radius * SPLASH_FLATTEN
            scope.drawOval(
                color = splashColor[index],
                topLeft = Offset(splashX[index] - radius, splashY[index] - flat),
                size = Size(radius * 2f, flat * 2f),
                alpha = 1f - progress,
                style = splashStroke,
            )
        }
    }

    private fun land(index: Int) {
        val landedOnSurface = landingY[index] <= height
        if (visible[index] && landedOnSurface && splashAge.isNotEmpty()) {
            val slot = nextSplash
            nextSplash = (nextSplash + 1) % splashAge.size
            splashX[slot] = x[index]
            splashY[slot] = landingY[index]
            splashMaxRadius[slot] = splashRadius[index]
            splashColor[slot] = color[index]
            splashAge[slot] = 0f
        }
        y[index] = -random.nextFloat() * height * RESPAWN_SPREAD
        x[index] = random.nextFloat() * width
        visible[index] = random.nextFloat() < intensity
    }

    /**
     * Nearer drops land lower: the surface reads as ground seen from above. The nearest can land
     * past the bottom edge, where they fall out of sight without a splash.
     */
    private fun landingFor(depth: Float): Float {
        val spread = (random.nextFloat() - HALF) * LANDING_JITTER
        return height * (LANDING_TOP + LANDING_RANGE * depth + spread)
    }

    private fun baseSlant(): Float = style.wind.coerceIn(-1f, 1f) * MAX_SLANT

    private fun advancePhases(phases: FloatArray, periods: FloatArray, step: Float) {
        for (index in phases.indices) {
            phases[index] = (phases[index] + step / periods[index]) % 1f
        }
    }

    /**
     * Two sine waves of unrelated periods added and scaled to `-1..1`, so the motion never quite
     * repeats within a sitting.
     */
    private fun wave(phases: FloatArray): Float {
        val first = sin(TWO_PI * phases[0])
        val second = sin(TWO_PI * phases[1] + PHASE)
        return first * FIRST_WEIGHT + second * (1f - FIRST_WEIGHT)
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
        const val FALL_RANGE_DP_PER_SECOND: Float = 480f
        const val MIN_THICKNESS: Float = 0.5f
        const val THICKNESS_RANGE: Float = 0.9f
        const val MAX_SLANT: Float = 0.35f
        const val MAX_GUST_SLANT: Float = 0.3f
        const val RESPAWN_SPREAD: Float = 0.25f
        const val LANDING_TOP: Float = 0.35f
        const val LANDING_RANGE: Float = 0.75f
        const val LANDING_JITTER: Float = 0.2f
        const val MAX_SPLASHES: Int = 48
        const val SPLASH_MILLIS: Float = 420f
        const val MIN_SPLASH: Float = 0.4f
        const val SPLASH_RANGE: Float = 0.9f
        const val MIN_SPLASH_START: Float = 0.2f
        const val SPLASH_FLATTEN: Float = 0.3f
        const val SPLASH_STROKE: Float = 0.7f
        const val FREE: Float = -1f
        const val HALF: Float = 0.5f
        const val FIRST_WEIGHT: Float = 0.6f
        const val PHASE: Float = 1.3f
        const val TWO_PI: Float = 6.2831855f
        const val MAX_STEP_MILLIS: Float = 50f
        const val MILLIS_PER_SECOND: Float = 1000f

        val GUST_PERIODS: FloatArray = floatArrayOf(7_300f, 2_900f)
        val SHOWER_PERIODS: FloatArray = floatArrayOf(23_000f, 9_700f)
    }
}
