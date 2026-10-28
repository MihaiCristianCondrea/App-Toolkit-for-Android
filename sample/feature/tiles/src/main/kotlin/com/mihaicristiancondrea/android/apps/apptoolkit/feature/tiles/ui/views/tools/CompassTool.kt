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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import kotlin.math.roundToInt

/**
 * Compass: a rose of ticks every 5°, degree numbers every 30° and the four cardinal names that turns against the
 * heading, so the fixed pointer in the middle always points where the phone points. Matches `compassRose()` in
 * `marketing/ui/src/kit/tool-bodies.js`; the rose is laid out on a 200-unit square, as the web one is.
 */
@Composable
fun CompassTool(azimuth: Float) {
    val heading = azimuth.roundToInt().mod(FULL_TURN)
    val direction = stringResource(DIRECTIONS[((heading + 22) / 45) % DIRECTIONS.size])
    // The rose turns the short way round: 359° to 1° is a 2° turn, not 358°.
    var turn by remember { mutableFloatStateOf(azimuth) }
    LaunchedEffect(azimuth) {
        turn += ((azimuth - turn) % FULL_TURN + 540f) % FULL_TURN - 180f
    }
    val rotation by animateFloatAsState(
        targetValue = turn,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "compass-rose",
    )

    val accent = ToolkitTileIcon.Compass.iconColors()
    val north = if (isSystemInDarkTheme()) Color(0xFFFFB88E) else Color(0xFFFF8B26)
    val colors = RoseColors(
        face = accent.container.copy(alpha = 0.1f).compositeOver(MaterialTheme.colorScheme.surfaceContainerLowest),
        tick = MaterialTheme.colorScheme.onSurfaceVariant,
        majorTick = MaterialTheme.colorScheme.onSurface,
        north = north,
        pointerOutline = MaterialTheme.colorScheme.surfaceContainerLowest,
    )
    val names = CARDINALS.map { stringResource(it) }
    val textMeasurer = rememberTextMeasurer()
    val description = stringResource(R.string.tool_compass_heading_description, heading, direction)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .size(272.dp)
                .semantics { contentDescription = description },
        ) {
            drawRose(rotation, names, colors, textMeasurer)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.tile_service_azimuth_format, heading),
                style = MaterialTheme.typography.displayMedium.copy(fontFeatureSettings = "tnum"),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = direction,
                style = MaterialTheme.typography.headlineSmall,
                color = accent.content,
            )
            Text(
                text = stringResource(R.string.tool_compass_magnetic_north),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private class RoseColors(
    val face: Color,
    val tick: Color,
    val majorTick: Color,
    val north: Color,
    val pointerOutline: Color,
)

private fun DrawScope.drawRose(rotation: Float, names: List<String>, colors: RoseColors, textMeasurer: TextMeasurer) {
    // One unit of the 200-unit square the rose is designed on.
    val u = size.minDimension / 200f
    val top = center.y - 100f * u
    drawCircle(colors.face, radius = 98f * u)

    rotate(-rotation) {
        for (step in 0 until FULL_TURN / 5) {
            val degrees = step * 5
            val (length, width, color) = when {
                degrees == 0 -> Triple(9f, 3f, colors.north)
                degrees % 30 == 0 -> Triple(9f, 2.2f, colors.majorTick)
                degrees % 10 == 0 -> Triple(6f, 1.2f, colors.tick.copy(alpha = 0.7f))
                else -> Triple(4f, 0.8f, colors.tick.copy(alpha = 0.45f))
            }
            rotate(degrees.toFloat()) {
                drawLine(
                    color = color,
                    start = Offset(center.x, top + 6f * u),
                    end = Offset(center.x, top + (6f + length) * u),
                    strokeWidth = width * u,
                    cap = StrokeCap.Round,
                )
            }
        }
        val numberStyle = TextStyle(color = colors.tick, fontSize = (9f * u).toSp(), fontWeight = FontWeight.Medium)
        for (degrees in DEGREE_LABELS) {
            rotate(degrees.toFloat()) { drawCentered(textMeasurer, degrees.toString(), numberStyle, top + 19f * u) }
        }
        names.forEachIndexed { index, name ->
            val style = TextStyle(
                color = if (index == 0) colors.north else colors.majorTick,
                fontSize = (11f * u).toSp(),
                fontWeight = FontWeight.Bold,
            )
            rotate(index * 90f) { drawCentered(textMeasurer, name, style, top + 34f * u) }
        }
    }

    // The lubber mark and the pointer stay still: they are the phone.
    val lubber = Path().apply {
        moveTo(center.x, top)
        lineTo(center.x + 5f * u, top + 9f * u)
        lineTo(center.x - 5f * u, top + 9f * u)
        close()
    }
    drawPath(lubber, colors.north)
    val pointer = Path().apply {
        moveTo(center.x, top + 62f * u)
        lineTo(center.x + 24f * u, top + 128f * u)
        lineTo(center.x, top + 116f * u)
        lineTo(center.x - 24f * u, top + 128f * u)
        close()
    }
    drawPath(pointer, colors.majorTick)
    drawPath(pointer, colors.pointerOutline, style = Stroke(width = 2f * u, join = StrokeJoin.Round))
}

private fun DrawScope.drawCentered(textMeasurer: TextMeasurer, text: String, style: TextStyle, y: Float) {
    val layout = textMeasurer.measure(text, style)
    drawText(layout, topLeft = Offset(center.x - layout.size.width / 2f, y))
}

private const val FULL_TURN = 360
private val DEGREE_LABELS = listOf(30, 60, 120, 150, 210, 240, 300, 330)
private val CARDINALS = listOf(
    R.string.tool_compass_north,
    R.string.tool_compass_east,
    R.string.tool_compass_south,
    R.string.tool_compass_west,
)
private val DIRECTIONS = listOf(
    R.string.tool_compass_north,
    R.string.tool_compass_northeast,
    R.string.tool_compass_east,
    R.string.tool_compass_southeast,
    R.string.tool_compass_south,
    R.string.tool_compass_southwest,
    R.string.tool_compass_west,
    R.string.tool_compass_northwest,
)

@Preview(showBackground = true)
@Composable
private fun CompassToolPreview() {
    MaterialTheme {
        Surface {
            CompassTool(azimuth = 312f)
        }
    }
}
