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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTile
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.BreathingState
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.MemoryInfo
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.NetworkTraffic
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.BatteryTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.BreathingTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.ClipboardTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.CoinFlipTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.CompassTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.CounterTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.DiceRollTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.GenericToolPreview
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.LevelTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.LuxMeterTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.MemoryTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.components.NetworkTrafficTool
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitSensorData
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

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
    sensorData: ToolkitSensorData,
    breathingState: BreathingState,
    memoryInfo: MemoryInfo?,
    networkTraffic: NetworkTraffic?,
    onClose: () -> Unit,
    onAddTile: () -> Unit,
    onSetupTile: () -> Unit,
) {
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        ) {
            ToolSheetHeader(tile = tile, onClose = onClose)
            ToolStatusSummary(tile = tile)
            HorizontalDivider()
            ToolInteractiveContent(
                tile = tile,
                sensorData = sensorData,
                breathingState = breathingState,
                memoryInfo = memoryInfo,
                networkTraffic = networkTraffic,
            )
            ToolSheetActions(
                tile = tile,
                onAddTile = onAddTile,
                onSetupTile = onSetupTile,
            )
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
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(id = R.string.tool_dialog_close_content_description),
            )
        }
    }
}

@Composable
private fun ToolStatusSummary(tile: ToolkitTile) {
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
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                Text(
                    text = stringResource(id = tile.status.helperTitleResId()),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = stringResource(id = tile.status.helperSummaryResId()),
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
    sensorData: ToolkitSensorData,
    breathingState: BreathingState,
    memoryInfo: MemoryInfo?,
    networkTraffic: NetworkTraffic?,
) {
    when (tile.id) {
        "coin_flip" -> CoinFlipTool()
        "dice_roll" -> DiceRollTool()
        "counter" -> CounterTool()
        "clipboard" -> ClipboardTool()
        "battery" -> BatteryTool()
        "compass" -> CompassTool(azimuth = sensorData.compassAzimuth)
        "bubble_level" -> LevelTool(pitch = sensorData.levelPitch, roll = sensorData.levelRoll)
        "lux_meter" -> LuxMeterTool(lux = sensorData.luxLevel)
        "breathing" -> BreathingTool(state = breathingState)
        "memory" -> MemoryTool(info = memoryInfo)
        "network_traffic" -> NetworkTrafficTool(traffic = networkTraffic)
        else -> GenericToolPreview(tile = tile)
    }
}

@Composable
private fun ToolSheetActions(
    tile: ToolkitTile,
    onAddTile: () -> Unit,
    onSetupTile: () -> Unit,
) {
    val hasAddAction = tile.requestKey != null
    val hasSetupAction = tile.status != ToolkitTileStatus.Available
    if (!hasAddAction && !hasSetupAction) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        if (hasAddAction) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = onAddTile,
                enabled = tile.status == ToolkitTileStatus.Available,
            ) {
                Text(text = stringResource(id = R.string.tiles_add))
            }
        }
        if (hasSetupAction) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onSetupTile,
            ) {
                Text(text = stringResource(id = R.string.tiles_setup))
            }
        }
    }
}

private fun ToolkitTileStatus.helperTitleResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tool_status_added_title
    ToolkitTileStatus.Available -> R.string.tool_status_available_title
    ToolkitTileStatus.NeedsSetup -> R.string.tool_status_needs_setup_title
    ToolkitTileStatus.Unsupported -> R.string.tool_status_unsupported_title
}

private fun ToolkitTileStatus.helperSummaryResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tool_status_added_summary
    ToolkitTileStatus.Available -> R.string.tool_status_available_summary
    ToolkitTileStatus.NeedsSetup -> R.string.tool_status_needs_setup_summary
    ToolkitTileStatus.Unsupported -> R.string.tool_status_unsupported_summary
}
