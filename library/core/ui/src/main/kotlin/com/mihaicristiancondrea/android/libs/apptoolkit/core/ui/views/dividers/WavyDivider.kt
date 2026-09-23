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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * A horizontal divider drawn as the toolkit's wavy line, `il_wavy_line`.
 *
 * It fills the available width and adapts to whatever it is given. The wave scales with
 * [waveHeight], keeping the drawable's proportions, and its wavelength is stretched or squeezed
 * slightly so a whole number of half-waves fits the width. Both ends therefore finish cleanly on
 * the centre line at any width, instead of the wave being cut off mid-curve.
 *
 * Like Material's `HorizontalDivider` it is decorative and adds nothing to the semantics tree.
 *
 * @param modifier applied to the divider. Constrain its width here to shorten it.
 * @param waveHeight the band the wave occupies, crest to trough including the stroke. The wave is
 * longer when this is taller, so the curve keeps its shape.
 * @param thickness the stroke width of the line.
 * @param color the line colour. Defaults to the colour of Material's plain dividers.
 */
@Composable
fun HorizontalWavyDivider(
    modifier: Modifier = Modifier,
    waveHeight: Dp = WavyDividerDefaults.WaveSize,
    thickness: Dp = WavyDividerDefaults.Thickness,
    color: Color = WavyDividerDefaults.color,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(waveHeight)
            .drawWavyLine(vertical = false, thickness = thickness, color = color),
    )
}

/**
 * A vertical divider drawn as the toolkit's wavy line, running top to bottom.
 *
 * It fills the available height and adapts to it the same way [HorizontalWavyDivider] adapts to
 * its width. Give it a bounded height, for example inside a `Row` with
 * `Modifier.height(IntrinsicSize.Min)`, since an unbounded one has nothing to fill.
 *
 * @param modifier applied to the divider. Constrain its height here to shorten it.
 * @param waveWidth the band the wave occupies, side to side including the stroke.
 * @param thickness the stroke width of the line.
 * @param color the line colour. Defaults to the colour of Material's plain dividers.
 */
@Composable
fun VerticalWavyDivider(
    modifier: Modifier = Modifier,
    waveWidth: Dp = WavyDividerDefaults.WaveSize,
    thickness: Dp = WavyDividerDefaults.Thickness,
    color: Color = WavyDividerDefaults.color,
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(waveWidth)
            .drawWavyLine(vertical = true, thickness = thickness, color = color),
    )
}

/** Defaults for [HorizontalWavyDivider] and [VerticalWavyDivider], taken from `il_wavy_line`. */
object WavyDividerDefaults {

    /** The band the wave occupies across the line: the drawable's 8dp height. */
    val WaveSize: Dp = 8.dp

    /** The drawable's 1dp stroke. */
    val Thickness: Dp = 1.dp

    /** The colour of Material's plain dividers, so a wavy divider sits among them naturally. */
    val color: Color
        @Composable get() = DividerDefaults.color
}

/**
 * Builds the wave once per size and redraws it from that cache.
 *
 * The path only depends on the size and the stroke width, so neither scrolling nor a colour change
 * rebuilds it.
 */
private fun Modifier.drawWavyLine(vertical: Boolean, thickness: Dp, color: Color): Modifier =
    drawWithCache {
        val strokeWidth: Float = thickness.toPx()
        val geometry: WavyLineGeometry = wavyLineGeometry(
            length = if (vertical) size.height else size.width,
            breadth = if (vertical) size.width else size.height,
            strokeWidth = strokeWidth,
        )
        val path: Path = geometry.toPath(vertical = vertical)
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Square)
        onDrawBehind {
            drawPath(path = path, color = color, style = stroke)
        }
    }

/**
 * The measurements of one wavy line, in pixels, along an axis of any direction.
 *
 * The line runs from [start] along its length in [halfWaveCount] half-waves of [halfWaveLength],
 * each a cubic curve that leaves and rejoins the centre line at [centre]. The curve's two control
 * points sit a third and two thirds of the way along, [controlOffset] off the centre line on
 * alternating sides, which is exactly how `il_wavy_line` is drawn.
 */
@Immutable
internal data class WavyLineGeometry(
    val start: Float,
    val centre: Float,
    val halfWaveCount: Int,
    val halfWaveLength: Float,
    val controlOffset: Float,
)

/**
 * Fits the wave of `il_wavy_line` to a line [length] long and [breadth] across.
 *
 * In the drawable's 8-unit tall viewport a half-wave is 15.2 units long, its control points sit
 * 4.67 units off the centre line, and the stroke is 1 unit wide. A cubic curve peaks at three
 * quarters of its control offset, so the crest reaches 3.5 units, and with half the stroke on top
 * it just touches the edge of the band. The same rule is kept here for any stroke: the crest plus
 * half the stroke fills half the breadth. The square caps reach half a stroke past each end, so
 * the line is inset by that much to stay inside its bounds.
 */
internal fun wavyLineGeometry(length: Float, breadth: Float, strokeWidth: Float): WavyLineGeometry {
    val halfStroke: Float = strokeWidth / 2f
    val drawnLength: Float = max(length - strokeWidth, 0f)
    val naturalHalfWaveLength: Float = breadth * HALF_WAVE_LENGTH_PER_BREADTH
    val halfWaveCount: Int = if (naturalHalfWaveLength > 0f) {
        max((drawnLength / naturalHalfWaveLength).roundToInt(), 1)
    } else {
        1
    }
    val crest: Float = max(breadth / 2f - halfStroke, 0f)

    return WavyLineGeometry(
        start = halfStroke,
        centre = breadth / 2f,
        halfWaveCount = halfWaveCount,
        halfWaveLength = drawnLength / halfWaveCount,
        controlOffset = crest / CUBIC_PEAK_PER_CONTROL_OFFSET,
    )
}

/** Traces [WavyLineGeometry] as a path, along x, or along y when [vertical]. */
private fun WavyLineGeometry.toPath(vertical: Boolean): Path {
    fun point(along: Float, across: Float): Pair<Float, Float> =
        if (vertical) across to along else along to across

    val path = Path()
    val (startX, startY) = point(along = start, across = centre)
    path.moveTo(startX, startY)

    var along: Float = start
    repeat(halfWaveCount) { index ->
        val side: Float = if (index % 2 == 0) -1f else 1f
        val across: Float = centre + side * controlOffset
        val (firstX, firstY) = point(along = along + halfWaveLength / 3f, across = across)
        val (secondX, secondY) = point(along = along + halfWaveLength * 2f / 3f, across = across)
        along += halfWaveLength
        val (endX, endY) = point(along = along, across = centre)
        path.cubicTo(firstX, firstY, secondX, secondY, endX, endY)
    }
    return path
}

/** `il_wavy_line`: a 15.2-unit half-wave in an 8-unit tall band. */
private const val HALF_WAVE_LENGTH_PER_BREADTH: Float = 15.2f / 8f

/** A cubic curve with both control points at the same offset peaks at three quarters of it. */
private const val CUBIC_PEAK_PER_CONTROL_OFFSET: Float = 0.75f
