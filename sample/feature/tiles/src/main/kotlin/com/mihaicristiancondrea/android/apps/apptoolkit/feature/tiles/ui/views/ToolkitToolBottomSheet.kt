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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.helperSummaryResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.helperTitleResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.BreathingToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.CaffeineToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.CoinFlipToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.CompassToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.CounterToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.DiceRollToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.FlashDimmerToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.LevelToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.LuxMeterToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.MorseToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.MusicSearchToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.SosToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.SoundModeToolRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileIconBadge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.previews.GenericToolPreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

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
            ToolInteractiveContent(tile = tile)
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
    val helperTitle = stringResource(id = tile.status.helperTitleResId())
    val helperSummary = stringResource(id = tile.status.helperSummaryResId())

    val customInfo = when (tile.id) {
        ToolkitTileIds.LUX_METER -> stringResource(id = R.string.tile_lux_meter_info)
        else -> null
    }

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
                if (customInfo != null) {
                    Text(
                        text = customInfo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
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
}

@Composable
private fun ToolInteractiveContent(
    tile: ToolkitTile,
) {
    when (tile.id) {
        ToolkitTileIds.COIN_FLIP -> CoinFlipToolRoute()
        ToolkitTileIds.DICE_ROLL -> DiceRollToolRoute()
        ToolkitTileIds.COUNTER -> CounterToolRoute()
        ToolkitTileIds.COMPASS -> CompassToolRoute()
        ToolkitTileIds.BUBBLE_LEVEL -> LevelToolRoute()
        ToolkitTileIds.LUX_METER -> LuxMeterToolRoute()
        ToolkitTileIds.CAFFEINE -> CaffeineToolRoute()
        ToolkitTileIds.SOUND_MODE -> SoundModeToolRoute()
        ToolkitTileIds.MUSIC_SEARCH -> MusicSearchToolRoute()
        ToolkitTileIds.SOS -> SosToolRoute()
        ToolkitTileIds.MORSE -> MorseToolRoute()
        ToolkitTileIds.BREATHING -> BreathingToolRoute()
        ToolkitTileIds.FLASH_DIMMER -> FlashDimmerToolRoute()
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

