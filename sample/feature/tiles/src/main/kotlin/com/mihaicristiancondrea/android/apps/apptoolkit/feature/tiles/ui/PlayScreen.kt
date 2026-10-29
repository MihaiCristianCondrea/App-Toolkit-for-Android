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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.workspaceTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CoinFlipToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.DiceRollToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.PlayTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ToolkitToolBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWorkspace
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolPair
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWidget
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileIconBadge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.DecisionWheelVisual
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CoinFlipTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.DiceRollTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.requestQuickSettingsTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlayScreen() {
    val catalogViewModel: ToolkitTilesViewModel = koinViewModel()
    val catalogState by catalogViewModel.state.collectAsStateWithLifecycle()
    LifecycleResumeEffect(catalogViewModel) {
        catalogViewModel.onEvent(ToolkitTilesEvent.Refresh)
        onPauseOrDispose { }
    }
    val catalogTools = catalogState.workspaceTools(PlayTools)
    val coin: CoinFlipToolViewModel = koinViewModel()
    val dice: DiceRollToolViewModel = koinViewModel()
    val random: RandomNumberToolViewModel = koinViewModel()
    val wheel: WheelToolViewModel = koinViewModel()
    val flip by coin.state.collectAsStateWithLifecycle()
    val roll by dice.state.collectAsStateWithLifecycle()
    val pick by random.state.collectAsStateWithLifecycle()
    val spin by wheel.state.collectAsStateWithLifecycle()
    ScreenHost(screen = AppScreenTracking.Screens.PLAY) {
        PlayScreenContent(flip, roll, pick, spin, contentPadding(), LocalShellSearch.current?.query.orEmpty(),
            onFlip = { coin.onEvent(CoinFlipToolEvent.Flip) }, onRoll = { dice.onEvent(DiceRollToolEvent.Roll) },
            catalogTools = catalogTools, onTileAdded = { catalogViewModel.onEvent(ToolkitTilesEvent.TileAdded(it)) })
    }
}

@Composable
internal fun PlayScreenContent(coin: CoinFlipToolUiState, dice: DiceRollToolUiState,
    random: RandomNumberToolUiState, wheel: WheelToolUiState, padding: PaddingValues, searchQuery: String = "",
    onFlip: () -> Unit = {}, onRoll: () -> Unit = {},
    catalogTools: List<ToolkitTile> = PlayTools, onTileAdded: (String) -> Unit = {},
) {
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val resources = LocalResources.current
    val query = searchQuery.trim()
    val tools = catalogTools.filter { query.isBlank() || resources.getString(it.titleResId).contains(query,true) || resources.getString(it.summaryResId).contains(query,true) }
    ToolWorkspace(padding) {
        if (tools.isEmpty()) Text(stringResource(R.string.tiles_filter_empty))
        else ToolSection(stringResource(R.string.section_chance), Icons.Outlined.Casino, Modifier.animateVisibility(0)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                tools.firstOrNull { it.id == ToolkitTileIds.DECISION_WHEEL }?.let { tile ->
                    Surface(onClick = { selectedId = tile.id }, shape = RoundedCornerShape(28.dp,28.dp,if (tools.size == 1) 28.dp else 8.dp,if (tools.size == 1) 28.dp else 8.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                        Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(stringResource(tile.titleResId), style = MaterialTheme.typography.labelLarge)
                                Text(wheel.winner ?: stringResource(R.string.wheel_ready), style = MaterialTheme.typography.headlineSmall)
                            }
                            DecisionWheelVisual(wheel, compact = true)
                        }
                    }
                }
                val chance = tools.filterNot { it.id == ToolkitTileIds.DECISION_WHEEL }
                chance.firstOrNull { it.id == ToolkitTileIds.RANDOM_NUMBER }?.let { tile ->
                    ToolWidget(tile, { selectedId = tile.id }, Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            TileIconBadge(tile.icon, large = true)
                            Text(random.result?.toString() ?: "?", style = MaterialTheme.typography.displaySmall)
                            Text(stringResource(R.string.tool_random_range, random.minimum.toLongOrNull() ?: 1L, random.maximum.toLongOrNull() ?: 100L), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                val pair = chance.filterNot { it.id == ToolkitTileIds.RANDOM_NUMBER }
                if (pair.isNotEmpty()) ToolPair(pair, top = tools.size == pair.size) { tile, shape ->
                    ToolWidget(tile, { selectedId = tile.id }, Modifier.weight(1f).fillMaxHeight(), shape) {
                        if (tile.id == ToolkitTileIds.COIN_FLIP) {
                            CoinFlipTool(coin.side, coin.request, onFlip, compact = true)
                            Text(stringResource(if (coin.side == com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.CoinSide.Heads) R.string.tile_service_heads else R.string.tile_service_tails))
                        } else {
                            DiceRollTool(result = dice.result, rollRequest = dice.request, onRoll = onRoll, compact = true)
                            Text(stringResource(R.string.tool_dice_roll_action))
                        }
                    }
                }
            }
        }
    }
    catalogTools.firstOrNull { it.id == selectedId }?.let { tile ->
        ToolkitToolBottomSheet(tile, onClose = { selectedId = null }, onAddTile = { tile.requestKey?.let { requestQuickSettingsTile(context,it, onTileAdded = onTileAdded) } }, onSetupTile = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayScreenPreview() {
    MaterialTheme { Surface { PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues()) } }
}
