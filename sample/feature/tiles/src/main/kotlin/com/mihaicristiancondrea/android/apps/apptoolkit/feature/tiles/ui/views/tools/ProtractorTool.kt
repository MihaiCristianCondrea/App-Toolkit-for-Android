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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ProtractorToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.protractorGeometry
import kotlin.math.cos
import kotlin.math.sin

/** Full-screen edge alignment, with a side control dock when the window is wide. */
@Composable
fun ProtractorTool(state: ProtractorToolUiState, onEvent: (ProtractorToolEvent) -> Unit, onClose: () -> Unit) {
    val latestEvent by rememberUpdatedState(onEvent)
    val color = MaterialTheme.colorScheme.onSurface
    val sectorColor = MaterialTheme.colorScheme.primaryContainer
    val accent = MaterialTheme.colorScheme.primary
    val dialColor = MaterialTheme.colorScheme.surfaceContainerLow
    val surfaceColor = MaterialTheme.colorScheme.surface
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = color)
    val textMeasurer = rememberTextMeasurer()
    val title = stringResource(R.string.tile_protractor_title)
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val geometry = protractorGeometry(maxWidth.value, maxHeight.value)
                Canvas(Modifier.fillMaxSize().pointerInput(geometry) {
                    detectTapGestures { point -> latestEvent(ProtractorToolEvent.AngleChanged(geometry.angleAt(point.x / density, point.y / density))) }
                }.pointerInput(geometry) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        latestEvent(ProtractorToolEvent.AngleChanged(geometry.angleAt(change.position.x / density, change.position.y / density)))
                    }
                }) {
                    val pivot = Offset(geometry.x.dp.toPx(), geometry.y.dp.toPx())
                    val radius = geometry.radius.dp.toPx()
                    fun point(degrees: Float, distance: Float): Offset {
                        val angle = Math.toRadians(degrees.toDouble())
                        return if (geometry.landscape) pivot + Offset(cos(angle).toFloat(), -sin(angle).toFloat()) * distance
                        else pivot + Offset(sin(angle).toFloat(), -cos(angle).toFloat()) * distance
                    }
                    val dial = Path().apply {
                        moveTo(pivot.x, pivot.y)
                        for (degree in 0..180) { val p = point(degree.toFloat(), radius); lineTo(p.x, p.y) }
                        close()
                    }
                    drawPath(dial, dialColor)
                    val sector = Path().apply {
                        moveTo(pivot.x, pivot.y)
                        for (degree in 0..state.angle.toInt()) { val p = point(degree.toFloat(), radius); lineTo(p.x, p.y) }
                        close()
                    }
                    drawPath(sector, sectorColor)
                    drawArc(color.copy(alpha = .4f), if (geometry.landscape) 180f else -90f, 180f, false,
                        pivot - Offset(radius, radius), Size(radius * 2, radius * 2), style = Stroke(1.dp.toPx()))
                    for (degree in 0..180) {
                        val tick = if (degree % 10 == 0) 20.dp.toPx() else if (degree % 5 == 0) 12.dp.toPx() else 6.dp.toPx()
                        drawLine(color.copy(alpha = .7f), point(degree.toFloat(), radius - tick), point(degree.toFloat(), radius), 1.dp.toPx())
                        if (degree % (if (radius < 200.dp.toPx()) 30 else 10) == 0) {
                            val label = textMeasurer.measure(degree.toString(), labelStyle)
                            val p = point(degree.toFloat(), radius - 38.dp.toPx())
                            // Kept on screen: 0 and 180 sit on the edge the pivot is on.
                            drawText(label, topLeft = Offset(
                                (p.x - label.size.width / 2f).coerceIn(4.dp.toPx(), (size.width - label.size.width - 4.dp.toPx()).coerceAtLeast(4.dp.toPx())),
                                (p.y - label.size.height / 2f).coerceIn(4.dp.toPx(), (size.height - label.size.height - 4.dp.toPx()).coerceAtLeast(4.dp.toPx())),
                            ))
                        }
                    }
                    drawLine(accent, pivot, point(0f, radius), 4.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(accent, pivot, point(state.angle, radius), 4.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(accent, 9.dp.toPx(), pivot)
                    val endpoint = point(state.angle, radius)
                    drawCircle(accent, 8.dp.toPx(), endpoint)
                    drawCircle(surfaceColor, 8.dp.toPx(), endpoint, style = Stroke(3.dp.toPx()))
                }
                Row(Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    IconButton(onClick = onClose) { Icon(ToolSymbols.Close, stringResource(R.string.tool_dialog_close_content_description)) }
                }
                // In landscape the three-button navigation sits at the side: the dock keeps clear of it.
                val dock = if (geometry.landscape) Modifier.align(Alignment.CenterEnd).navigationBarsPadding().displayCutoutPadding().width(280.dp).padding(24.dp)
                    else Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(20.dp)
                Column(dock.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.tool_angle_value, state.angle), style = MaterialTheme.typography.displayMedium, color = accent)
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                        Text(stringResource(when {
                            state.angle == 0f -> R.string.angle_zero
                            state.angle == 90f -> R.string.angle_right
                            state.angle == 180f -> R.string.angle_straight
                            state.angle < 90f -> R.string.angle_acute
                            else -> R.string.angle_obtuse
                        }), style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                        }
                    }
                    val sliderState = rememberSliderState(value = state.angle, trackRange = 0f..180f)
                    sliderState.value = state.angle
                    Slider(state = sliderState, onValueChange = { onEvent(ProtractorToolEvent.AngleChanged(it)) }, modifier = Modifier.semantics { contentDescription = title })
                    Row(Modifier.fillMaxWidth()) {
                        listOf(0f,45f,90f,180f).forEach { angle ->
                            TextButton(onClick = { onEvent(ProtractorToolEvent.AngleChanged(angle)) }, modifier = Modifier.weight(1f)) { Text(angle.toInt().toString()+"°") }
                        }
                    }
                }
            }
        }
    }
}
