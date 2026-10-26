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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ProtractorToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun ProtractorTool(state: ProtractorToolUiState, onEvent: (ProtractorToolEvent) -> Unit) {
    val latestEvent by rememberUpdatedState(onEvent)
    val color = MaterialTheme.colorScheme.onSurface
    val accent = MaterialTheme.colorScheme.primary
    val textStyle = MaterialTheme.typography.labelSmall.copy(color = color)
    val textMeasurer = rememberTextMeasurer()
    val angleLabel = stringResource(R.string.tile_protractor_title)
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        Text(stringResource(R.string.tool_angle_value, state.angle), style = MaterialTheme.typography.displayMedium)
        Canvas(
            Modifier.fillMaxWidth().height(240.dp)
                .pointerInput(Unit) {
                    detectTapGestures { point ->
                        val pivot = Offset(size.width / 2f, size.height - 24.dp.toPx())
                        latestEvent(ProtractorToolEvent.AngleChanged(angleAt(point, pivot)))
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val pivot = Offset(size.width / 2f, size.height - 24.dp.toPx())
                        latestEvent(ProtractorToolEvent.AngleChanged(angleAt(change.position, pivot)))
                        change.consume()
                    }
                },
        ) {
            val pivot = Offset(size.width / 2f, size.height - 24.dp.toPx())
            val radius = min(size.width / 2f - 28.dp.toPx(), size.height - 48.dp.toPx()).coerceAtLeast(1f)
            val origin = Offset(pivot.x - radius, pivot.y - radius)
            val diameter = Size(radius * 2f, radius * 2f)
            drawArc(color, 180f, 180f, false, origin, diameter, style = Stroke(1.dp.toPx()))
            for (degrees in 0..180 step 5) {
                val radians = Math.toRadians(degrees.toDouble())
                fun point(distance: Float) = Offset(
                    pivot.x + cos(radians).toFloat() * distance,
                    pivot.y - sin(radians).toFloat() * distance,
                )
                val tick = if (degrees % 30 == 0) 12.dp.toPx() else 6.dp.toPx()
                drawLine(color, point(radius - tick), point(radius), 1.dp.toPx())
                if (degrees % 30 == 0) {
                    val label = textMeasurer.measure(degrees.toString(), textStyle)
                    val position = point(radius + 14.dp.toPx())
                    drawText(label, topLeft = position - Offset(label.size.width / 2f, label.size.height / 2f))
                }
            }
            val radians = Math.toRadians(state.angle.toDouble())
            val arm = Offset(pivot.x + cos(radians).toFloat() * radius, pivot.y - sin(radians).toFloat() * radius)
            drawLine(accent, pivot, Offset(pivot.x + radius, pivot.y), 3.dp.toPx())
            drawLine(accent, pivot, arm, 3.dp.toPx())
            drawCircle(accent, 5.dp.toPx(), pivot)
        }
        Text(stringResource(R.string.tool_protractor_hint), style = MaterialTheme.typography.bodyMedium)
        val sliderState = rememberSliderState(value = state.angle, trackRange = 0f..180f)
        sliderState.value = state.angle
        Slider(
            state = sliderState,
            onValueChange = { onEvent(ProtractorToolEvent.AngleChanged(it)) },
            modifier = Modifier.semantics { contentDescription = angleLabel },
        )
    }
}

private fun angleAt(point: Offset, pivot: Offset): Float {
    val radians = atan2((pivot.y - point.y).coerceAtLeast(0f), point.x - pivot.x)
    return Math.toDegrees(radians.toDouble()).toFloat()
}
