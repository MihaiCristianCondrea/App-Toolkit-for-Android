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
import android.os.Build
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerUnits
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.abs

/**
 * A ruler at the screen's real scale, drawn flush with the screen's edges so an object can be laid
 * against the bezel.
 *
 * It follows the display: the scales run down the long side in portrait and along it in landscape,
 * and their spacing comes from the display's physical pixel density on that axis, corrected by the
 * user's calibration. By default centimetres run along one edge and inches along the other.
 *
 * The simple ruler is only the ruler and a reading: drag anywhere to measure from zero. The advanced
 * ruler adds a measurement range between two handles and the settings that change the scales: which
 * units, which edge, where zero is, how fine the inch steps are, and the calibration.
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
        FullScreenRulerWindow()
        val latestState by rememberUpdatedState(state)
        val latestEvent by rememberUpdatedState(onEvent)
        val density = LocalDensity.current
        val metrics = LocalView.current.resources.displayMetrics
        val settings = state.settings
        val textMeasurer = rememberTextMeasurer()
        val colors = RulerColors(
            line = MaterialTheme.colorScheme.onSurface,
            marker = MaterialTheme.colorScheme.primary,
            strip = MaterialTheme.colorScheme.surfaceContainerLow,
        )
        val labelStyle = MaterialTheme.typography.labelMedium

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            val landscape = maxWidth > maxHeight
            fun dpiOf(value: Float) = value.takeIf { it.isFinite() && it > 0f } ?: (density.density * 160f)
            val pixelsPerMillimetre = dpiOf(if (landscape) metrics.xdpi else metrics.ydpi) / 25.4f * state.scale
            val lengthPx = with(density) { (if (landscape) maxWidth else maxHeight).toPx() }
            val scales = rulerScales(settings)
            val geometry = RulerGeometry(landscape, settings.countFromEnd, lengthPx)

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(landscape, settings.countFromEnd, settings.advanced, lengthPx) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            val current = latestState
                            val startHandle = abs(geometry.fraction(down.position) - current.start) <
                                abs(geometry.fraction(down.position) - current.end)
                            fun move(position: Offset) {
                                val fraction = geometry.fraction(position)
                                val now = latestState
                                when {
                                    !now.settings.advanced -> latestEvent(RulerToolEvent.MarkerMoved(fraction))
                                    startHandle -> latestEvent(RulerToolEvent.RangeChanged(fraction.coerceAtMost(now.end), now.end))
                                    else -> latestEvent(RulerToolEvent.RangeChanged(now.start, fraction.coerceAtLeast(now.start)))
                                }
                            }
                            move(down.position)
                            drag(down.id) { change ->
                                move(change.position)
                                change.consume()
                            }
                        }
                    },
            ) {
                val thickness = RulerStripWidth.toPx()
                for ((_, edge) in scales) {
                    drawStrip(edge, geometry, thickness, colors.strip)
                }
                if (settings.advanced) {
                    val from = geometry.along(state.start)
                    val to = geometry.along(state.end)
                    drawBand(geometry, minOf(from, to), maxOf(from, to), colors.marker.copy(alpha = 0.14f))
                    drawReadingLine(geometry, from, colors.marker, 3.dp.toPx())
                    drawReadingLine(geometry, to, colors.marker, 3.dp.toPx())
                } else {
                    val zero = geometry.along(0f)
                    val marker = geometry.along(state.marker)
                    drawBand(geometry, minOf(zero, marker), maxOf(zero, marker), colors.marker.copy(alpha = 0.08f))
                    drawReadingLine(geometry, marker, colors.marker, 2.dp.toPx())
                }
                for ((units, edge) in scales) {
                    drawScale(
                        units = units,
                        edge = edge,
                        geometry = geometry,
                        pixelsPerMillimetre = pixelsPerMillimetre,
                        inchDivisions = settings.inchDivisions,
                        color = colors.line,
                        labelStyle = labelStyle.copy(color = colors.line),
                        textMeasurer = textMeasurer,
                    )
                }
            }

            val reading = (if (settings.advanced) state.end - state.start else state.marker) * lengthPx / pixelsPerMillimetre
            val edges = scales.map { it.second }.toSet()
            val inset = Modifier.padding(
                start = if (!landscape && RulerEdge.Start in edges) RulerStripWidth else 0.dp,
                end = if (!landscape && RulerEdge.End in edges) RulerStripWidth else 0.dp,
                top = if (landscape && RulerEdge.Start in edges) RulerStripWidth else 0.dp,
                bottom = if (landscape && RulerEdge.End in edges) RulerStripWidth else 0.dp,
            )
            Box(Modifier.fillMaxSize().then(inset).safeDrawingPadding()) {
                RulerPanel(state, reading, landscape, onEvent, onClose)
            }
        }
    }
}

/** Lays the dialog's window over the whole display, cutout included, and hides the system bars. */
@Composable
private fun FullScreenRulerWindow() {
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.parent as? DialogWindowProvider)?.window
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
        val controller = window?.let { WindowCompat.getInsetsController(it, view) }
        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller?.hide(WindowInsetsCompat.Type.systemBars())
        onDispose { controller?.show(WindowInsetsCompat.Type.systemBars()) }
    }
}

/**
 * The ruler's controls, the same in both modes: the title, the Simple / Advanced switch and the
 * reading stay where they are, and the advanced settings unfold under them. In simple mode the
 * panel is see-through, so a drag anywhere outside the reading moves the reading line.
 */
@Composable
private fun RulerPanel(
    state: RulerToolUiState,
    millimetres: Float,
    landscape: Boolean,
    onEvent: (RulerToolEvent) -> Unit,
    onClose: () -> Unit,
) {
    val settings = state.settings
    val background by animateColorAsState(
        targetValue = if (settings.advanced) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0f),
        label = "rulerPanel",
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .verticalScroll(rememberScrollState(), enabled = settings.advanced)
            .padding(SizeConstants.MediumSize),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.tile_ruler_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            GeneralButton(
                style = GeneralButtonStyle.Text,
                onClick = onClose,
                icon = ToolkitIcon.Vector(ToolSymbols.Close),
                contentDescription = stringResource(R.string.tool_dialog_close_content_description),
            )
        }
        RulerChoice(
            options = persistentListOf(stringResource(R.string.tool_ruler_simple), stringResource(R.string.tool_ruler_advanced)),
            selected = if (settings.advanced) 1 else 0,
            onSelect = { onEvent(RulerToolEvent.AdvancedChanged(it == 1)) },
        )
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RulerReading(settings.units, millimetres)
                AnimatedVisibility(visible = !settings.advanced) {
                    Text(
                        text = stringResource(R.string.tool_ruler_drag_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp).widthIn(max = 200.dp),
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = settings.advanced,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        ) {
            RulerSettingsPanel(state, landscape, onEvent)
        }
    }
}

/** The reading in the units the ruler shows; with both scales, millimetres first and inches under them. */
@Composable
private fun RulerReading(units: RulerUnits, millimetres: Float) {
    val metric = stringResource(R.string.tool_ruler_mm_value, millimetres)
    val imperial = stringResource(R.string.tool_ruler_inches_value, millimetres / 25.4f)
    Text(
        text = if (units == RulerUnits.Imperial) imperial else metric,
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.primary,
    )
    if (units == RulerUnits.Both) {
        Text(text = imperial, style = MaterialTheme.typography.titleLarge)
    }
}

/** The advanced ruler's settings: scales, measurement range and calibration. */
@Composable
private fun RulerSettingsPanel(
    state: RulerToolUiState,
    landscape: Boolean,
    onEvent: (RulerToolEvent) -> Unit,
) {
    val settings = state.settings
    val rangeLabel = stringResource(R.string.tool_ruler_range)
    val calibrationLabel = stringResource(R.string.tool_ruler_calibration)
    val first = stringResource(if (landscape) R.string.tool_ruler_top else R.string.tool_ruler_left)
    val second = stringResource(if (landscape) R.string.tool_ruler_bottom else R.string.tool_ruler_right)
    val zeroFirst = stringResource(if (landscape) R.string.tool_ruler_left else R.string.tool_ruler_top)
    val zeroSecond = stringResource(if (landscape) R.string.tool_ruler_right else R.string.tool_ruler_bottom)

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        RulerSetting(stringResource(R.string.tool_ruler_units)) {
            RulerChoice(
                options = persistentListOf(
                    stringResource(R.string.tool_ruler_units_metric),
                    stringResource(R.string.tool_ruler_units_imperial),
                    stringResource(R.string.tool_ruler_units_both),
                ),
                selected = settings.units.ordinal,
                onSelect = { onEvent(RulerToolEvent.UnitsSelected(RulerUnits.entries[it])) },
            )
        }
        RulerSetting(stringResource(R.string.tool_ruler_edge)) {
            RulerChoice(
                options = persistentListOf(first, second),
                selected = settings.edge.ordinal,
                onSelect = { onEvent(RulerToolEvent.EdgeSelected(RulerEdge.entries[it])) },
            )
        }
        RulerSetting(stringResource(R.string.tool_ruler_zero)) {
            RulerChoice(
                options = persistentListOf(zeroFirst, zeroSecond),
                selected = if (settings.countFromEnd) 1 else 0,
                onSelect = { onEvent(RulerToolEvent.CountFromEndChanged(it == 1)) },
            )
        }
        if (settings.units != RulerUnits.Metric) {
            RulerSetting(stringResource(R.string.tool_ruler_inch_steps)) {
                RulerChoice(
                    options = RulerSettings.InchDivisions.map { stringResource(R.string.tool_ruler_inch_step, it) }.toPersistentList(),
                    selected = RulerSettings.InchDivisions.indexOf(settings.inchDivisions),
                    onSelect = { onEvent(RulerToolEvent.InchDivisionsSelected(RulerSettings.InchDivisions[it])) },
                )
            }
        }

        RulerSetting(rangeLabel) {
            val rangeSliderState = rememberRangeSliderState(startValue = state.start, endValue = state.end)
            rangeSliderState.startValue = state.start
            rangeSliderState.endValue = state.end
            RangeSlider(
                state = rangeSliderState,
                onValueChange = { onEvent(RulerToolEvent.RangeChanged(it.start, it.endInclusive)) },
                modifier = Modifier.semantics { contentDescription = rangeLabel },
            )
        }

        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(SizeConstants.MediumSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(calibrationLabel, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                    GeneralButton(
                        style = GeneralButtonStyle.Text,
                        enabled = state.scale != 1f,
                        onClick = { onEvent(RulerToolEvent.ResetScale) },
                        label = stringResource(R.string.tool_ruler_reset),
                    )
                }
                // The scales are the reference: they follow the slider, so a real ruler held against one shows when they agree.
                Text(text = stringResource(R.string.tool_ruler_reference), style = MaterialTheme.typography.bodySmall)
                val scaleSliderState = rememberSliderState(value = state.scale, trackRange = 0.5f..2f)
                scaleSliderState.value = state.scale
                Slider(
                    state = scaleSliderState,
                    onValueChange = { onEvent(RulerToolEvent.ScaleChanged(it)) },
                    onValueChangeFinished = { onEvent(RulerToolEvent.SaveScale) },
                    modifier = Modifier.semantics { contentDescription = calibrationLabel },
                )
                if (state.saveFailed) {
                    Text(text = stringResource(R.string.tool_ruler_save_failed), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun RulerSetting(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        content()
    }
}

/** A row of segmented choices without check marks, so three of them fit between two scales. */
@Composable
private fun RulerChoice(options: ImmutableList<String>, selected: Int, onSelect: (Int) -> Unit) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selected,
                onClick = { if (index != selected) onSelect(index) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
            ) {
                Text(text = label, maxLines = 1)
            }
        }
    }
}

private class RulerColors(val line: Color, val marker: Color, val strip: Color)

/** The scales to draw and the edge each sits on: with both units, inches take the edge opposite centimetres. */
internal fun rulerScales(settings: RulerSettings): List<Pair<RulerUnits, RulerEdge>> = when (settings.units) {
    RulerUnits.Metric -> listOf(RulerUnits.Metric to settings.edge)
    RulerUnits.Imperial -> listOf(RulerUnits.Imperial to settings.edge)
    RulerUnits.Both -> listOf(RulerUnits.Metric to settings.edge, RulerUnits.Imperial to settings.edge.opposite)
}

/**
 * Maps between a distance from zero, as a fraction of the ruler's length, and the screen. The ruler
 * runs down the screen in portrait and across it in landscape, from either end.
 */
internal class RulerGeometry(val landscape: Boolean, val countFromEnd: Boolean, val length: Float) {
    /** The position along the ruler of a distance from zero given as a fraction of its length. */
    fun along(fraction: Float): Float = if (countFromEnd) length * (1f - fraction) else length * fraction

    /** The distance from zero of a point on screen, as a fraction of the ruler's length. */
    fun fraction(point: Offset): Float {
        val raw = ((if (landscape) point.x else point.y) / length.coerceAtLeast(1f)).coerceIn(0f, 1f)
        return if (countFromEnd) 1f - raw else raw
    }

    /** A point at [along] on the ruler and [across] in from [edge]. */
    fun point(along: Float, across: Float, edge: RulerEdge, size: Size): Offset {
        val cross = if (edge == RulerEdge.Start) across else (if (landscape) size.height else size.width) - across
        return if (landscape) Offset(along, cross) else Offset(cross, along)
    }
}

private fun DrawScope.drawStrip(edge: RulerEdge, geometry: RulerGeometry, thickness: Float, color: Color) {
    val topLeft = geometry.point(0f, if (edge == RulerEdge.Start) 0f else thickness, edge, size)
    val stripSize = if (geometry.landscape) Size(size.width, thickness) else Size(thickness, size.height)
    val origin = if (geometry.landscape) Offset(0f, topLeft.y) else Offset(topLeft.x, 0f)
    drawRect(color, topLeft = origin, size = stripSize)
}

private fun DrawScope.drawBand(geometry: RulerGeometry, from: Float, to: Float, color: Color) {
    if (geometry.landscape) {
        drawRect(color, topLeft = Offset(from, 0f), size = Size(to - from, size.height))
    } else {
        drawRect(color, topLeft = Offset(0f, from), size = Size(size.width, to - from))
    }
}

private fun DrawScope.drawReadingLine(geometry: RulerGeometry, at: Float, color: Color, width: Float) {
    if (geometry.landscape) {
        drawLine(color, Offset(at, 0f), Offset(at, size.height), width)
    } else {
        drawLine(color, Offset(0f, at), Offset(size.width, at), width)
    }
}

/**
 * One scale against [edge]: millimetres with longer ticks at 5 and 10 and a number at each
 * centimetre, or inches split into [inchDivisions] with ticks that shorten with each halving and a
 * number at each inch. The unit's name stands at zero.
 */
private fun DrawScope.drawScale(
    units: RulerUnits,
    edge: RulerEdge,
    geometry: RulerGeometry,
    pixelsPerMillimetre: Float,
    inchDivisions: Int,
    color: Color,
    labelStyle: TextStyle,
    textMeasurer: TextMeasurer,
) {
    val metric = units == RulerUnits.Metric
    val perMajor = if (metric) 10 else inchDivisions
    val step = if (metric) pixelsPerMillimetre else pixelsPerMillimetre * 25.4f / inchDivisions
    if (step <= 0f) return
    val count = (geometry.length / step).toInt().coerceIn(0, 4000)
    val unitName = if (metric) "cm" else "in"
    for (tick in 0..count) {
        val distance = tick * step
        val at = geometry.along(distance / geometry.length)
        val length: Dp = when {
            tick % perMajor == 0 -> 28.dp
            tick % (perMajor / 2) == 0 -> if (metric) 20.dp else 22.dp
            !metric && perMajor >= 4 && tick % (perMajor / 4) == 0 -> 17.dp
            !metric && perMajor >= 8 && tick % (perMajor / 8) == 0 -> 13.dp
            else -> if (metric) 12.dp else 9.dp
        }
        drawLine(
            color = color,
            start = geometry.point(at, 0f, edge, size),
            end = geometry.point(at, length.toPx(), edge, size),
            strokeWidth = if (tick % perMajor == 0) 1.5.dp.toPx() else 1.dp.toPx(),
        )
        if (tick % perMajor == 0) {
            val label = textMeasurer.measure(if (tick == 0) unitName else (tick / perMajor).toString(), labelStyle)
            val across = length.toPx() + 4.dp.toPx() + (if (geometry.landscape) label.size.height else label.size.width) / 2f
            val centre = geometry.point(at, across, edge, size)
            val maxX = (size.width - label.size.width).coerceAtLeast(0f)
            val maxY = (size.height - label.size.height).coerceAtLeast(0f)
            drawText(
                textLayoutResult = label,
                topLeft = Offset(
                    (centre.x - label.size.width / 2f).coerceIn(0f, maxX),
                    (centre.y - label.size.height / 2f).coerceIn(0f, maxY),
                ),
            )
        }
    }
}

private val RulerStripWidth = 64.dp
