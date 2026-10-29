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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
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
            Triple(R.string.section_length, Icons.Outlined.SquareFoot, listOf(ToolkitTileIds.RULER, ToolkitTileIds.PROTRACTOR)),
            Triple(R.string.section_convert, Icons.Outlined.SwapHoriz, listOf(ToolkitTileIds.UNIT_CONVERTER, ToolkitTileIds.BILL_SPLITTER)),
            Triple(R.string.section_count, Icons.Outlined.Timer, listOf(ToolkitTileIds.COUNTER, ToolkitTileIds.REACTION_TEST)),
        )
        sections.forEachIndexed { sectionIndex, (title, icon, ids) ->
            val visible = tools.filter { it.id in ids }
            if (visible.isNotEmpty()) ToolSection(stringResource(title), icon, Modifier.animateVisibility(sectionIndex)) {
                if (title == R.string.section_count) Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    visible.forEachIndexed { index, tile ->
                        ToolWidget(tile, { selectedId = tile.id; startReaction = false }, Modifier.fillMaxWidth(),
                            toolCorners(0, 1, top = index == 0, bottom = index == visible.lastIndex),
                            headerAction = if (tile.id == ToolkitTileIds.COUNTER) { { TextButton(onClick = { onCounter(CounterToolEvent.Reset) }, enabled = counterState.count > 0) { Text(stringResource(R.string.tool_counter_reset)) } } } else null) {
                            if (tile.id == ToolkitTileIds.COUNTER) CounterControls(counterState.count,
                                onIncrement = { onCounter(CounterToolEvent.Increment) },
                                onDecrement = { onCounter(CounterToolEvent.Decrement) })
                            else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(reaction.bestTimeMs?.let { stringResource(R.string.tool_reaction_test_best, it) }
                                    ?: stringResource(R.string.tool_reaction_test_idle_prompt), style = MaterialTheme.typography.bodyLarge)
                                FilledTonalButton(onClick = { startReaction = true; selectedId = tile.id }) { Text(stringResource(R.string.tool_reaction_test_start)) }
                            }
                        }
                    }
                } else ToolPair(visible) { tile, shape ->
                    ToolWidget(tile, { selectedId = tile.id }, Modifier.weight(1f).fillMaxHeight(), shape,
                        when (tile.id) {
                            ToolkitTileIds.UNIT_CONVERTER -> MaterialTheme.colorScheme.secondaryContainer
                            ToolkitTileIds.BILL_SPLITTER -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceContainerHigh
                        }) {
                        when (tile.id) {
                            ToolkitTileIds.RULER -> { MeasureFace(false); Text("mm / in", style = MaterialTheme.typography.headlineSmall) }
                            ToolkitTileIds.PROTRACTOR -> { MeasureFace(true, protractorAngle); Text(stringResource(R.string.tool_angle_value, protractorAngle), style = MaterialTheme.typography.headlineSmall) }
                            ToolkitTileIds.UNIT_CONVERTER -> {
                                Text(converter.left+" "+converter.from.symbol, style = MaterialTheme.typography.headlineSmall)
                                Icon(Icons.Outlined.SwapHoriz, null)
                                Text(converter.right+" "+converter.to.symbol, style = MaterialTheme.typography.headlineSmall)
                            }
                            ToolkitTileIds.BILL_SPLITTER -> {
                                Text(bill.result?.let { formatBillAmount(it.perPerson, bill.currency, locale) } ?: "—", style = MaterialTheme.typography.headlineLarge)
                                Text(stringResource(R.string.tool_bill_per_person), style = MaterialTheme.typography.bodySmall)
                                Text(if (bill.tipEnabled) stringResource(R.string.tool_tip_percent, bill.tipPercent) else stringResource(R.string.no_tip))
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

@Composable
private fun MeasureFace(protractor: Boolean, angle: Float = 45f) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(104.dp)) {
        if (protractor) {
            drawArc(color.copy(alpha = .15f), 180f, 180f, true, Offset(8.dp.toPx(), 8.dp.toPx()),
                androidx.compose.ui.geometry.Size(size.width - 16.dp.toPx(), (size.height - 8.dp.toPx()) * 2f))
            val radians = Math.toRadians(angle.toDouble())
            val radius = minOf(size.width / 2f, size.height - 8.dp.toPx())
            drawLine(color, Offset(center.x, size.height), Offset(center.x + kotlin.math.cos(radians).toFloat() * radius, size.height - kotlin.math.sin(radians).toFloat() * radius), 3.dp.toPx())
        } else {
            drawRoundRect(color.copy(alpha = .15f), topLeft = Offset(0f, 12.dp.toPx()), size = androidx.compose.ui.geometry.Size(size.width, 64.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()))
            for (i in 0..20) drawLine(color, Offset(size.width * i / 20f, 12.dp.toPx()), Offset(size.width * i / 20f, (if (i % 5 == 0) 36 else 24).dp.toPx()), 1.dp.toPx())
        }
    }
}

internal val NumbersCalculateTools = MeasureTools.filter { it.id == ToolkitTileIds.UNIT_CONVERTER || it.id == ToolkitTileIds.BILL_SPLITTER }
internal val NumbersChanceTools = MeasureTools.filter { it.id == ToolkitTileIds.COUNTER || it.id == ToolkitTileIds.REACTION_TEST }
internal val NumbersSecondaryTools = MeasureTools

@Preview(showBackground = true)
@Composable
private fun NumbersScreenPreview() {
    MaterialTheme { Surface { NumbersScreenContent(CounterToolUiState(count = 42), PaddingValues()) } }
}
