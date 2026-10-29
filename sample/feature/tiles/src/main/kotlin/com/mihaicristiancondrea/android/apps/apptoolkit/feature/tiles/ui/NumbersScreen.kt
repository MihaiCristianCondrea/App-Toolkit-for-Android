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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolDesignTheme

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.labelResId
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.workspaceTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CounterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.MeasureTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BillSplitterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.formatBillAmount
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ToolkitToolBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWorkspace
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolPair
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWidget
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.toolCorners
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CounterControls
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.requestQuickSettingsTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NumbersScreen() {
    val catalogViewModel: ToolkitTilesViewModel = koinViewModel()
    val catalogState by catalogViewModel.state.collectAsStateWithLifecycle()
    LifecycleResumeEffect(catalogViewModel) {
        catalogViewModel.onEvent(ToolkitTilesEvent.Refresh)
        onPauseOrDispose { }
    }
    val catalogTools = catalogState.workspaceTools(MeasureTools)
    val counter: CounterToolViewModel = koinViewModel()
    val bill: BillSplitterToolViewModel = koinViewModel()
    val converter: UnitConverterToolViewModel = koinViewModel()
    val reaction: ReactionTestToolViewModel = koinViewModel()
    val protractor: ProtractorToolViewModel = koinViewModel()
    val count by counter.state.collectAsStateWithLifecycle()
    val split by bill.state.collectAsStateWithLifecycle()
    val conversion by converter.state.collectAsStateWithLifecycle()
    val reflex by reaction.state.collectAsStateWithLifecycle()
    val angle by protractor.state.collectAsStateWithLifecycle()
    ScreenHost(screen = AppScreenTracking.Screens.NUMBERS) {
        NumbersScreenContent(count, contentPadding(), split, conversion, reflex,
            searchQuery = LocalShellSearch.current?.query.orEmpty(), onCounter = counter::onEvent, protractorAngle = angle.angle,
            catalogTools = catalogTools, onTileAdded = { catalogViewModel.onEvent(ToolkitTilesEvent.TileAdded(it)) })
    }
}

@Composable
internal fun NumbersScreenContent(
    counterState: CounterToolUiState, contentPadding: PaddingValues,
    bill: BillSplitterToolUiState = BillSplitterToolUiState(), converter: UnitConverterToolUiState = UnitConverterToolUiState(),
    reaction: ReactionTestToolUiState = ReactionTestToolUiState(), searchQuery: String = "",
    onCounter: (CounterToolEvent) -> Unit = {},
    protractorAngle: Float = 45f,
    catalogTools: List<ToolkitTile> = MeasureTools,
    onTileAdded: (String) -> Unit = {},
) {
    ToolDesignTheme {

        var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
        var startReaction by rememberSaveable { mutableStateOf(false) }
        val resources = LocalResources.current
        val locale = LocalConfiguration.current.locales[0]
        val context = LocalContext.current
        val query = searchQuery.trim()
        val tools = catalogTools.filter { query.isBlank() || resources.getString(it.titleResId).contains(query, true) || resources.getString(it.summaryResId).contains(query, true) }
        ToolWorkspace(contentPadding) {
            if (tools.isEmpty()) Text(stringResource(R.string.tiles_filter_empty))
            val sections = listOf(
                Triple(R.string.section_length, ToolSymbols.Rounded.SquareFoot, listOf(ToolkitTileIds.RULER, ToolkitTileIds.PROTRACTOR)),
                Triple(R.string.section_convert, ToolSymbols.Rounded.SwapHoriz, listOf(ToolkitTileIds.UNIT_CONVERTER, ToolkitTileIds.BILL_SPLITTER)),
                Triple(R.string.section_count, ToolSymbols.Rounded.Timer, listOf(ToolkitTileIds.COUNTER, ToolkitTileIds.REACTION_TEST)),
            )
            sections.forEach { (title, icon, ids) ->
                val visible = tools.filter { it.id in ids }
                if (visible.isNotEmpty()) ToolSection(stringResource(title), icon, Modifier) {
                    if (title == R.string.section_count) Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        visible.forEachIndexed { index, tile ->
                            val shape = toolCorners(0, 1, top = index == 0, bottom = index == visible.lastIndex)
                            if (tile.id == ToolkitTileIds.COUNTER) {
                                ToolWidget(tile, { selectedId = tile.id; startReaction = false }, Modifier.fillMaxWidth(), shape,
                                    headerAction = {
                                        TextButton(onClick = { onCounter(CounterToolEvent.Reset) }, enabled = counterState.count > 0) {
                                            Icon(ToolSymbols.Refresh, null, Modifier.size(18.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text(stringResource(R.string.tool_counter_reset))
                                        }
                                    },
                                    padding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
                                    spacing = 8.dp) {
                                    CounterControls(counterState.count,
                                        onIncrement = { onCounter(CounterToolEvent.Increment) },
                                        onDecrement = { onCounter(CounterToolEvent.Decrement) })
                                }
                            } else ReactionWidget(tile, reaction.bestTimeMs, shape,
                                onOpen = { selectedId = tile.id; startReaction = false },
                                onStart = { startReaction = true; selectedId = tile.id })
                        }
                    } else ToolPair(visible) { tile, shape ->
                        ToolWidget(tile, { selectedId = tile.id }, Modifier.weight(1f).fillMaxHeight(), shape,
                            when (tile.id) {
                                ToolkitTileIds.UNIT_CONVERTER -> MaterialTheme.colorScheme.secondaryContainer
                                ToolkitTileIds.BILL_SPLITTER -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.surfaceContainerHigh
                            }) {
                            when (tile.id) {
                                ToolkitTileIds.RULER -> {
                                    MeasureFace(protractor = false)
                                    MeasureDetail("mm / in", stringResource(R.string.widget_ruler_note))
                                }
                                ToolkitTileIds.PROTRACTOR -> {
                                    MeasureFace(protractor = true, angle = protractorAngle)
                                    MeasureDetail(stringResource(R.string.tile_service_azimuth_format, protractorAngle.roundToInt()), stringResource(R.string.widget_protractor_note))
                                }
                                ToolkitTileIds.UNIT_CONVERTER -> {
                                    Column(Modifier.weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        ConversionLine(converter.left, converter.from.symbol)
                                        Icon(ToolSymbols.SwapVert, null, Modifier.size(20.dp))
                                        ConversionLine(converter.right, converter.to.symbol)
                                    }
                                    Text(stringResource(converter.category.labelResId()), style = WidgetDetailStyle)
                                }
                                ToolkitTileIds.BILL_SPLITTER -> {
                                    Icon(ToolSymbols.Rounded.Group, null, Modifier.padding(bottom = 4.dp).size(40.dp))
                                    Text(bill.result?.let { formatBillAmount(it.perPerson, bill.currency, locale) } ?: "—",
                                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, lineHeight = 38.sp), fontWeight = FontWeight.Medium, maxLines = 1)
                                    Text(stringResource(R.string.tool_bill_per_person).lowercase(locale), style = WidgetDetailStyle)
                                    Text(
                                        text = (if (bill.tipEnabled) stringResource(R.string.tool_tip_percent, bill.tipPercent) + " " + stringResource(R.string.tool_bill_tip).lowercase(locale) else stringResource(R.string.no_tip)) +
                                            " · " + (bill.people.toIntOrNull() ?: 1).let { pluralStringResource(R.plurals.widget_people, it, it) },
                                        style = WidgetDetailStyle,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        catalogTools.firstOrNull { it.id == selectedId }?.let { tile ->
            ToolkitToolBottomSheet(tile = tile, onClose = { selectedId = null; startReaction = false },
                startReaction = startReaction,
                onAddTile = { tile.requestKey?.let { requestQuickSettingsTile(context, it, onTileAdded = onTileAdded) } }, onSetupTile = {})
        }
    }
}

/** The reading under a Measure visual: the value in the primary colour, then what to do with it. */
@Composable
private fun MeasureDetail(value: String, note: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp, lineHeight = 34.sp), fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary, maxLines = 1)
        Text(note, style = WidgetDetailStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ConversionLine(value: String, unit: String) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp, lineHeight = 32.sp), fontWeight = FontWeight.Medium, maxLines = 1)
        Text(unit, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 3.dp))
    }
}

/** Reaction Test: the best time or a prompt, and Start, which opens the test already running. */
@Composable
private fun ReactionWidget(tile: ToolkitTile, bestMs: Long?, shape: Shape, onOpen: () -> Unit, onStart: () -> Unit) {
    Surface(onClick = onOpen, modifier = Modifier.fillMaxWidth().animateVisibility(), shape = shape, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(tile.titleResId), style = WidgetTitleStyle)
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = bestMs?.let { stringResource(R.string.tool_reaction_test_time_format, it.toInt()) } ?: stringResource(R.string.widget_reaction_ready),
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp, lineHeight = 32.sp, fontFeatureSettings = "tnum"),
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = stringResource(if (bestMs != null) R.string.widget_reaction_best else R.string.widget_reaction_prompt),
                        style = WidgetDetailStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 5.dp),
                    )
                }
            }
            FilledTonalButton(onClick = onStart) {
                Icon(ToolSymbols.PlayArrow, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.tool_reaction_test_start))
            }
        }
    }
}

/**
 * The ruler and protractor previews, drawn on the 180 x 104 grid of the marketing kit's widgets
 * (marketing/ui/src/screens/numbers.js), in the primary colour.
 */
@Composable
private fun MeasureFace(protractor: Boolean, angle: Float = 45f) {
    val color = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelMedium.copy(color = color, fontWeight = FontWeight.Medium)
    Canvas(Modifier.fillMaxWidth().height(104.dp)) {
        // One unit of the 180 x 104 drawing, centred and at most 200 dp wide.
        val u = minOf(size.width, 200.dp.toPx()) / 180f
        val left = (size.width - 180f * u) / 2f
        val top = (size.height - 104f * u) / 2f
        fun p(x: Float, y: Float) = Offset(left + x * u, top + y * u)
        if (protractor) {
            drawArc(color.copy(alpha = .12f), 180f, 180f, true, p(20f, 20f), Size(140f * u, 140f * u))
            for (i in 0..18) {
                val r = i * Math.PI / 18
                val inner = if (i % 3 == 0) 57f else 63f
                drawLine(color, p(90f + 70f * cos(r).toFloat(), 90f - 70f * sin(r).toFloat()), p(90f + inner * cos(r).toFloat(), 90f - inner * sin(r).toFloat()), (if (i % 3 == 0) 2f else 1f) * u)
            }
            val radians = Math.toRadians(angle.coerceIn(0f, 180f).toDouble())
            val arm = Path().apply {
                moveTo(p(155f, 90f).x, p(155f, 90f).y)
                lineTo(p(90f, 90f).x, p(90f, 90f).y)
                val end = p(90f + 65f * cos(radians).toFloat(), 90f - 65f * sin(radians).toFloat())
                lineTo(end.x, end.y)
            }
            drawPath(arm, color, style = Stroke(3f * u, cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawCircle(color, 4f * u, p(90f, 90f))
        } else {
            drawRoundRect(color.copy(alpha = .12f), topLeft = p(10f, 18f), size = Size(160f * u, 68f * u), cornerRadius = CornerRadius(12f * u))
            for (i in 0..20) {
                val x = 20f + i * 7f
                drawLine(color, p(x, 18f), p(x, if (i % 5 == 0) 42f else 30f), (if (i % 5 == 0) 2f else 1f) * u)
                if (i % 5 == 0) {
                    val text = textMeasurer.measure((i / 5).toString(), labelStyle)
                    drawText(text, topLeft = p(x, 65f) - Offset(text.size.width / 2f, text.size.height / 2f))
                }
            }
        }
    }
}

// Copied from the theme's styles so the widget type stays in the app font.
private val WidgetTitleStyle: TextStyle
    @Composable get() = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)
private val WidgetDetailStyle: TextStyle
    @Composable get() = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp)

internal val NumbersCalculateTools = MeasureTools.filter { it.id == ToolkitTileIds.UNIT_CONVERTER || it.id == ToolkitTileIds.BILL_SPLITTER }
internal val NumbersChanceTools = MeasureTools.filter { it.id == ToolkitTileIds.COUNTER || it.id == ToolkitTileIds.REACTION_TEST }
internal val NumbersSecondaryTools = MeasureTools

@Preview(showBackground = true)
@Composable
private fun NumbersScreenPreview() {
    MaterialTheme { Surface { NumbersScreenContent(CounterToolUiState(count = 42), PaddingValues()) } }
}
