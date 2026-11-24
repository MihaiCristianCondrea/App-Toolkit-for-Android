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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.layout.navigationBarsPadding
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.toolOpenedEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.helperSummaryResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.helperTitleResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BillSplitterToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BreathingToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CoinFlipToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CompassToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CounterToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.DiceRollToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.FlashDimmerToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.LevelToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.MorseToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ProtractorToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.RandomNumberToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ReactionTestToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.RulerToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.SosToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.UnitConverterToolSheet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BellToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BellToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.BellTool
import org.koin.compose.viewmodel.koinViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.WaterEjectToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.DecisionWheelToolSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileIconBadge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolAccentTheme
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.previews.GenericToolPreview
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.SuppressShakeToReport

/**
 * Animated bottom sheet host for previewing every Quick Tools catalog entry.
 *
 * Tile-ready entries mirror their Quick Settings behavior in-app, while unavailable or setup-only
 * entries explain why the catalog item is not interactive yet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolkitToolBottomSheet(
    tile: ToolkitTile,
    onClose: () -> Unit,
    onAddTile: () -> Unit,
    onSetupTile: () -> Unit,
    startReaction: Boolean = false,
) {
    val telemetry = LocalTelemetry.current
    LaunchedEffect(tile.id) {
        telemetry.logEvent(toolOpenedEvent(tile.id))
    }
    when (tile.id) {
        ToolkitTileIds.PROTRACTOR -> {
            ProtractorToolSheet(onClose = onClose)
            return
        }
        ToolkitTileIds.RULER -> {
            RulerToolSheet(onClose = onClose)
            return
        }
    }
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
        dragHandle = null,
    ) {
        ToolAccentTheme(tile.icon) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    // The sheet reaches behind the system navigation; its last control stays above it.
                    .navigationBarsPadding()
                    .padding(SizeConstants.LargeSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
            ) {
                if (tile.id == ToolkitTileIds.BELL) {
                    val bell: BellToolViewModel = koinViewModel()
                    val state = bell.state.collectAsStateWithLifecycle().value
                    SuppressShakeToReport()
                    LifecycleResumeEffect(bell) {
                        bell.onEvent(BellToolEvent.Open)
                        onPauseOrDispose { bell.onEvent(BellToolEvent.Close) }
                    }
                    ToolSheetHeader(
                        tile = if (state.motionAvailable) tile else tile.copy(summaryResId = R.string.bell_motion_unavailable),
                        onClose = onClose,
                    )
                    BellTool(state = state, onRing = { bell.onEvent(BellToolEvent.Ring) })
                } else {
                    ToolSheetHeader(tile = tile, onClose = onClose)
                    ToolInteractiveContent(tile = tile, startReaction = startReaction)
                }
                ToolIntegrationSection(
                    tile = tile,
                    onAddTile = onAddTile,
                    onSetupTile = onSetupTile,
                )
            }
        }
    }
}

@Composable
private fun ToolSheetHeader(
    tile: ToolkitTile,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TileIconBadge(icon = tile.icon, large = true)
        Spacer(modifier = Modifier.width(SizeConstants.LargeSize))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
        ) {
            Text(
                text = stringResource(id = tile.titleResId),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(id = tile.summaryResId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        GeneralButton(
            style = GeneralButtonStyle.Text,
            onClick = onClose,
            icon = ToolkitIcon.Vector(ToolSymbols.Close),
            contentDescription = stringResource(id = R.string.tool_dialog_close_content_description),
        )
    }
}

@Composable
private fun ToolStatusSummary(tile: ToolkitTile) {
    val helperTitle = stringResource(id = tile.status.helperTitleResId())
    val helperSummary = stringResource(id = tile.status.helperSummaryResId())

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SizeConstants.LargeSize),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.MediumSize),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ToolSymbols.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                Text(
                    text = helperTitle,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = helperSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ToolInteractiveContent(
    tile: ToolkitTile,
    startReaction: Boolean,
) {
    when (tile.id) {
        ToolkitTileIds.WATER_EJECT -> WaterEjectToolSheet()
        ToolkitTileIds.DECISION_WHEEL -> DecisionWheelToolSheet()
        ToolkitTileIds.COIN_FLIP -> CoinFlipToolSheet()
        ToolkitTileIds.DICE_ROLL -> DiceRollToolSheet()
        ToolkitTileIds.COUNTER -> CounterToolSheet()
        ToolkitTileIds.COMPASS -> CompassToolSheet()
        ToolkitTileIds.BUBBLE_LEVEL -> LevelToolSheet()
        ToolkitTileIds.REACTION_TEST -> ReactionTestToolSheet(autoStart = startReaction)
        ToolkitTileIds.SOS -> SosToolSheet()
        ToolkitTileIds.MORSE -> MorseToolSheet()
        ToolkitTileIds.BREATHING -> BreathingToolSheet()
        ToolkitTileIds.FLASH_DIMMER -> FlashDimmerToolSheet()
        ToolkitTileIds.PROTRACTOR -> Unit
        ToolkitTileIds.BILL_SPLITTER -> BillSplitterToolSheet()
        ToolkitTileIds.RANDOM_NUMBER -> RandomNumberToolSheet()
        ToolkitTileIds.UNIT_CONVERTER -> UnitConverterToolSheet()
        else -> GenericToolPreview(tile = tile)
    }
}

@Composable
private fun ToolIntegrationSection(
    tile: ToolkitTile,
    onAddTile: () -> Unit,
    onSetupTile: () -> Unit,
) {
    val hasAddAction = tile.requestKey != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val isUnsupported = tile.status == ToolkitTileStatus.Unsupported

    if (isUnsupported) {
        Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize)) {
            ToolStatusSummary(tile = tile)
            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                modifier = Modifier.fillMaxWidth(),
                measurements = ButtonMeasurements.Medium,
                onClick = onSetupTile,
                label = stringResource(id = R.string.tiles_setup),
            )
        }
        return
    }

    val canShowQsSection = tile.requestKey != null
    if (canShowQsSection) {
        QuickSettingsIntegrationCard(
            tile = tile,
            hasAddAction = hasAddAction,
            onAddTile = onAddTile
        )
    }
}

@Composable
private fun QuickSettingsIntegrationCard(
    tile: ToolkitTile,
    hasAddAction: Boolean,
    onAddTile: () -> Unit,
) {
    val isAdded = tile.status == ToolkitTileStatus.Added

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraSmallSize),
    ) {
        val infoShape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp,
            bottomStart = 8.dp,
            bottomEnd = 8.dp,
        )

        val actionShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 28.dp,
            bottomEnd = 28.dp,
        )

        val containerColor = MaterialTheme.colorScheme.secondaryContainer
        val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

        Surface(
            shape = if (hasAddAction && !isAdded) infoShape else RoundedCornerShape(28.dp),
            color = containerColor,
            contentColor = contentColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.LargeSize),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ToolSymbols.Widgets,
                    contentDescription = null,
                    tint = contentColor
                )
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                    val title = if (isAdded) {
                        stringResource(id = R.string.tool_status_added_title)
                    } else {
                        stringResource(id = R.string.tiles_qs_integration_title)
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                    )

                    val description = if (isAdded) {
                        stringResource(id = R.string.tool_status_added_summary)
                    } else {
                        stringResource(id = R.string.tiles_qs_integration_summary_format, stringResource(id = tile.titleResId))
                    }

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (hasAddAction && !isAdded) {
            GeneralButton(
                style = GeneralButtonStyle.Tonal,
                modifier = Modifier.fillMaxWidth(),
                measurements = ButtonMeasurements.Medium,
                onClick = onAddTile,
                label = stringResource(id = R.string.tiles_add_to_qs),
                icon = ToolkitIcon.Vector(ToolSymbols.Add),
                shape = actionShape,
                containerColor = containerColor,
                contentColor = contentColor,
            )
        }
    }
}
