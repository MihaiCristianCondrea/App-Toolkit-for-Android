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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.backgroundShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LiveToolsUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CompassTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.LevelTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import kotlin.math.roundToInt

@Composable
internal fun QuickToolsWidgets(tools: List<ToolkitTile>, live: LiveToolsUiState, onOpen: (ToolkitTile) -> Unit, onLevel: (Int) -> Unit) {
    if (tools.size == 1 && tools[0].id in listOf(ToolkitTileIds.WATER_EJECT, ToolkitTileIds.BREATHING)) {
        val tile = tools[0]
        ToolWidget(tile, { onOpen(tile) }, Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (tile.id == ToolkitTileIds.WATER_EJECT) com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.WaterEjectVisual(false, compact = true)
                else TileIconBadge(tile.icon, large = true)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (tile.id == ToolkitTileIds.WATER_EJECT) "165 Hz" else "4 · 2 · 4 · 1", style = MaterialTheme.typography.headlineSmall)
                    Text(stringResource(tile.summaryResId), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        return
    }
    ToolPair(tools) { tile, shape ->
        val colored = tile.id == ToolkitTileIds.FLASH_DIMMER || tile.id == ToolkitTileIds.SOS
        val colors = tile.icon.iconColors()
        ToolWidget(tile, { onOpen(tile) }, Modifier.weight(1f).fillMaxHeight(), shape,
            if (colored) colors.container.copy(alpha = .18f) else MaterialTheme.colorScheme.surfaceContainerHigh) {
            when (tile.id) {
                ToolkitTileIds.COMPASS -> CompassTool(live.heading, compact = true)
                ToolkitTileIds.BUBBLE_LEVEL -> LevelTool(live.pitch, live.roll, compact = true)
                else -> {
                    val torch = live.torch
                    val level = if (torch.capabilities.maximumLevel > 0) torch.currentLevel.toFloat() / torch.capabilities.maximumLevel else 0f
                    val latestLevel by rememberUpdatedState(onLevel)
                    val haptic = LocalHapticFeedback.current
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(Modifier.size(112.dp).clip(tile.icon.backgroundShape()).background(colors.container.copy(alpha = .18f))
                            .then(if (tile.id == ToolkitTileIds.FLASH_DIMMER && torch.capabilities.supportsDimming) Modifier.pointerInput(torch.capabilities.maximumLevel) {
                                detectVerticalDragGestures(onDragStart = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }) { change, _ ->
                                    change.consume()
                                    latestLevel(((1f - change.position.y / size.height) * torch.capabilities.maximumLevel).roundToInt().coerceIn(0, torch.capabilities.maximumLevel))
                                }
                            } else Modifier), contentAlignment = Alignment.Center) {
                            if (tile.id == ToolkitTileIds.FLASH_DIMMER) Box(Modifier.fillMaxWidth().fillMaxHeight(level.coerceIn(0f,1f)).align(Alignment.BottomCenter).background(colors.container))
                            Icon(tile.icon.imageVector(), null, Modifier.size(48.dp), tint = colors.content)
                        }
                    }
                    if (tile.id == ToolkitTileIds.FLASH_DIMMER) Text(stringResource(if (torch.isEnabled) R.string.flash_dimmer_on else R.string.flash_dimmer_off), style = MaterialTheme.typography.headlineSmall)
                    else if (tile.id == ToolkitTileIds.SOS) Text("··· −−− ···", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}
