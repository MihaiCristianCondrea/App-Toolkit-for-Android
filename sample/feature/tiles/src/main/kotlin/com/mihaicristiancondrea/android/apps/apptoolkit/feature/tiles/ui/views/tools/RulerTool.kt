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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberRangeSliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import kotlin.math.abs

/**
 * Full-screen ruler with graduations anchored flush against the left screen edge so physical
 * objects can be placed directly against the bezel.
 */
@Composable
fun RulerTool(
    state: RulerToolUiState,
    onEvent: (RulerToolEvent) -> Unit,
    onClose: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        val latestState by rememberUpdatedState(state)
        val latestEvent by rememberUpdatedState(onEvent)
        val density = LocalDensity.current
        val metrics = LocalView.current.resources.displayMetrics
        val dpi = metrics.ydpi.takeIf { it.isFinite() && it > 0 } ?: density.density * 160
        val pixelsPerMillimetre = dpi / 25.4f * state.scale
        var heightPx by remember { mutableFloatStateOf(1f) }
        val millimetres = (state.end - state.start) * heightPx / pixelsPerMillimetre
        val textMeasurer = rememberTextMeasurer()
        val lineColor = MaterialTheme.colorScheme.onSurface
        val markerColor = MaterialTheme.colorScheme.primary
        val bandColor = markerColor.copy(alpha = 0.14f)
        val textStyle = MaterialTheme.typography.labelSmall.copy(color = lineColor)
        val rangeLabel = stringResource(R.string.tool_ruler_range)
        val calibrationLabel = stringResource(R.string.tool_ruler_calibration)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Canvas(
                modifier = Modifier
                    .width(96.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .onSizeChanged { heightPx = it.height.toFloat().coerceAtLeast(1f) }
                    .pointerInput(Unit) {
                        var startHandle = true
                        detectVerticalDragGestures(
                            onDragStart = { point ->
                                val yFraction = point.y / size.height
                                startHandle = abs(yFraction - latestState.start) < abs(yFraction - latestState.end)
                            },
                        ) { change, _ ->
                            val fraction = (change.position.y / size.height).coerceIn(0f, 1f)
                            if (startHandle) {
                                latestEvent(
                                    RulerToolEvent.RangeChanged(
                                        fraction.coerceAtMost(latestState.end),
                                        latestState.end,
                                    )
                                )
                            } else {
                                latestEvent(
                                    RulerToolEvent.RangeChanged(
                                        latestState.start,
                                        fraction.coerceAtLeast(latestState.start),
                                    )
                                )
                            }
                            change.consume()
                        }
                    },
            ) {
                val startY = state.start * size.height
                val endY = state.end * size.height
                drawRect(
                    color = bandColor,
                    topLeft = Offset(0f, startY),
                    size = Size(size.width, (endY - startY).coerceAtLeast(0f)),
                )
                val step = if (state.inches) pixelsPerMillimetre * 25.4f / 16f else pixelsPerMillimetre
                val major = if (state.inches) 16 else 10
                val mid = major / 2
                val count = (size.height / step).toInt().coerceIn(0, 4000)
                repeat(count + 1) { tick ->
                    val y = tick * step
                    val tickLength = when {
                        tick % major == 0 -> 32.dp.toPx()
                        tick % mid == 0 -> 22.dp.toPx()
                        else -> 12.dp.toPx()
                    }
                    drawLine(lineColor, Offset(0f, y), Offset(tickLength, y), 1.dp.toPx())
                    if (tick % major == 0) {
                        val label = (tick / major).toString()
                        val measured = textMeasurer.measure(label, style = textStyle)
                        val textY = (y - measured.size.height / 2f)
                            .coerceIn(0f, (size.height - measured.size.height).coerceAtLeast(0f))
                        drawText(
                            textLayoutResult = measured,
                            topLeft = Offset(tickLength + 4.dp.toPx(), textY),
                        )
                    }
                }
                for (fraction in listOf(state.start, state.end)) {
                    val y = fraction * size.height
                    drawLine(markerColor, Offset(0f, y), Offset(size.width, y), 3.dp.toPx())
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(SizeConstants.LargeSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.tile_ruler_title),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f),
                    )
                    GeneralButton(
                        style = GeneralButtonStyle.Text,
                        onClick = onClose,
                        icon = ToolkitIcon.Vector(Icons.Outlined.Close),
                        contentDescription = stringResource(R.string.tool_dialog_close_content_description),
                    )
                }

                Text(
                    text = if (state.inches) {
                        stringResource(R.string.tool_ruler_inches_value, millimetres / 25.4f)
                    } else {
                        stringResource(R.string.tool_ruler_mm_value, millimetres)
                    },
                    style = MaterialTheme.typography.headlineLarge,
                )

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = !state.inches,
                        onClick = { if (state.inches) onEvent(RulerToolEvent.ToggleUnits) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    ) {
                        Text(text = stringResource(R.string.tool_ruler_mm))
                    }
                    SegmentedButton(
                        selected = state.inches,
                        onClick = { if (!state.inches) onEvent(RulerToolEvent.ToggleUnits) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    ) {
                        Text(text = stringResource(R.string.tool_ruler_inches))
                    }
                }

                Text(rangeLabel, style = MaterialTheme.typography.titleSmall)
                val rangeSliderState = rememberRangeSliderState(startValue = state.start, endValue = state.end)
                rangeSliderState.startValue = state.start
                rangeSliderState.endValue = state.end
                RangeSlider(
                    state = rangeSliderState,
                    onValueChange = { onEvent(RulerToolEvent.RangeChanged(it.start, it.endInclusive)) },
                    modifier = Modifier.semantics { contentDescription = rangeLabel },
                )

                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(SizeConstants.MediumSize),
                        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
                    ) {
                        Text(calibrationLabel, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = stringResource(R.string.tool_ruler_reference),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        // Use the ruler's vertical DPI and reserve the full reference length.
                        val referenceHeight = with(density) { (30 * pixelsPerMillimetre).toDp() }
                        Canvas(
                            Modifier.fillMaxWidth().height(with(density) { (30 * dpi / 25.4f * 2f).toDp() } + 8.dp),
                        ) {
                            val x = size.width / 2f
                            val top = 4.dp.toPx()
                            val bottom = top + 30 * pixelsPerMillimetre
                            val cap = 8.dp.toPx()
                            drawLine(markerColor, Offset(x, top), Offset(x, bottom), 2.dp.toPx())
                            drawLine(markerColor, Offset(x - cap, top), Offset(x + cap, top), 2.dp.toPx())
                            drawLine(markerColor, Offset(x - cap, bottom), Offset(x + cap, bottom), 2.dp.toPx())
                        }
                        val scaleSliderState = rememberSliderState(value = state.scale, trackRange = 0.5f..2f)
                        scaleSliderState.value = state.scale
                        Slider(
                            state = scaleSliderState,
                            onValueChange = { onEvent(RulerToolEvent.ScaleChanged(it)) },
                            onValueChangeFinished = { onEvent(RulerToolEvent.SaveScale) },
                            modifier = Modifier.semantics { contentDescription = calibrationLabel },
                        )
                        if (state.saveFailed) {
                            Text(
                                text = stringResource(R.string.tool_ruler_save_failed),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }
    }
}
