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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads.rememberOverviewNativeAd
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AdsConstants

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolDesignTheme

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.backgroundShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.CoinSide
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.WidgetLabel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.WidgetNote
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.WidgetValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    val query = LocalShellSearch.current?.query.orEmpty()
    val overviewAd = rememberOverviewNativeAd(
        placementKey = "play_overview",
        adUnitId = AdsConstants.NATIVE_AD_UNIT_ID,
        enabled = query.isBlank(),
    )
    ScreenHost(screen = AppScreenTracking.Screens.PLAY) {
        PlayScreenContent(flip, roll, pick, spin, contentPadding(), query, overviewAd = overviewAd,
            onFlip = { coin.onEvent(CoinFlipToolEvent.Flip) }, onRoll = { dice.onEvent(DiceRollToolEvent.Roll) },
            catalogTools = catalogTools, onTileAdded = { catalogViewModel.onEvent(ToolkitTilesEvent.TileAdded(it)) })
    }
}

@Composable
internal fun PlayScreenContent(coin: CoinFlipToolUiState, dice: DiceRollToolUiState,
    random: RandomNumberToolUiState, wheel: WheelToolUiState, padding: PaddingValues, searchQuery: String = "",
    onFlip: () -> Unit = {}, onRoll: () -> Unit = {},
    catalogTools: List<ToolkitTile> = PlayTools, onTileAdded: (String) -> Unit = {},
    overviewAd: (@Composable () -> Unit)? = null,
) {
    ToolDesignTheme {

        var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
        val context = LocalContext.current
        val resources = LocalResources.current
        val query = searchQuery.trim()
        val tools = catalogTools.filter { query.isBlank() || resources.getString(it.titleResId).contains(query,true) || resources.getString(it.summaryResId).contains(query,true) }
        ToolWorkspace(padding) {
            if (tools.isEmpty()) Text(stringResource(R.string.tiles_filter_empty))
            else ToolSection(stringResource(R.string.section_chance), ToolSymbols.Rounded.Casino, Modifier) {
                // The kit's Chance group (marketing/ui/src/screens/play.js): every card on the secondary container,
                // 28 dp on the group's outer corners and 8 dp between cards.
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val wheelTile = tools.firstOrNull { it.id == ToolkitTileIds.DECISION_WHEEL }
                    val randomTile = tools.firstOrNull { it.id == ToolkitTileIds.RANDOM_NUMBER }
                    val pair = tools.filter { it.id == ToolkitTileIds.COIN_FLIP || it.id == ToolkitTileIds.DICE_ROLL }
                    val rows = listOfNotNull(wheelTile, randomTile).size + if (pair.isEmpty()) 0 else 1
                    var row = 0
                    fun corners(): RoundedCornerShape {
                        val first = row == 0
                        val last = row == rows - 1
                        row++
                        return RoundedCornerShape(if (first) 28.dp else 8.dp, if (first) 28.dp else 8.dp, if (last) 28.dp else 8.dp, if (last) 28.dp else 8.dp)
                    }
                    wheelTile?.let { tile ->
                        ChanceCard(onClick = { selectedId = tile.id }, shape = corners()) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    WidgetLabel(stringResource(tile.titleResId), letterSpacing = 0.8f)
                                    Text(wheel.winner ?: stringResource(R.string.wheel_ready),
                                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp, lineHeight = 32.sp), fontWeight = FontWeight.Medium)
                                    val count = wheel.choices.size.takeIf { it >= 2 } ?: 2
                                    Text(pluralStringResource(R.plurals.widget_wheel_choices, count, count), style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp))
                                }
                                DecisionWheelVisual(wheel, compact = true)
                            }
                        }
                    }
                    randomTile?.let { tile ->
                        val gold = tile.icon.iconColors()
                        ChanceCard(onClick = { selectedId = tile.id }, shape = corners()) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    WidgetLabel(stringResource(tile.titleResId))
                                    WidgetValue(random.result?.toString() ?: "?", size = 48)
                                    WidgetNote(stringResource(R.string.tool_random_range, random.minimum.toLongOrNull() ?: 1L, random.maximum.toLongOrNull() ?: 100L), ToolkitTileIcon.Compass.iconColors().content)
                                }
                                Box(Modifier.size(88.dp).clip(tile.icon.backgroundShape()).background(lerp(MaterialTheme.colorScheme.surfaceContainerLowest, gold.container, .35f)), contentAlignment = Alignment.Center) {
                                    Icon(ToolSymbols.Numbers, null, Modifier.size(40.dp), tint = gold.content)
                                }
                            }
                        }
                    }
                    if (pair.isNotEmpty()) {
                        val shape = corners()
                        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            pair.forEachIndexed { index, tile ->
                                val cell = RoundedCornerShape(
                                    topStart = if (index == 0) shape.topStart else CornerSize(8.dp), topEnd = if (index == pair.lastIndex) shape.topEnd else CornerSize(8.dp),
                                    bottomEnd = if (index == pair.lastIndex) shape.bottomEnd else CornerSize(8.dp), bottomStart = if (index == 0) shape.bottomStart else CornerSize(8.dp),
                                )
                                ChanceCard(onClick = { selectedId = tile.id }, shape = cell, modifier = Modifier.weight(1f).fillMaxHeight(), centered = true) {
                                    WidgetLabel(stringResource(tile.titleResId), Modifier.align(Alignment.Start))
                                    if (tile.id == ToolkitTileIds.COIN_FLIP) {
                                        // Both visuals sit in the same 128 dp stage, so the two cards' values line up.
                                        Box(Modifier.size(128.dp), contentAlignment = Alignment.Center) { CoinFlipTool(coin.side, coin.request, onFlip, compact = true) }
                                        WidgetValue(stringResource(if (coin.side == CoinSide.Heads) R.string.tile_service_heads else R.string.tile_service_tails), size = 24)
                                        WidgetNote(stringResource(R.string.widget_coin_note), ToolkitTileIcon.Compass.iconColors().content)
                                    } else {
                                        Box(Modifier.size(128.dp), contentAlignment = Alignment.Center) { DiceRollTool(result = dice.result, rollRequest = dice.request, onRoll = onRoll, compact = true) }
                                        WidgetValue(stringResource(R.string.widget_dice_value), size = 24)
                                        WidgetNote(stringResource(R.string.widget_dice_note), ToolkitTileIcon.Compass.iconColors().content)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (query.isBlank() && tools.isNotEmpty()) overviewAd?.invoke()
        }
        catalogTools.firstOrNull { it.id == selectedId }?.let { tile ->
            ToolkitToolBottomSheet(tile, onClose = { selectedId = null }, onAddTile = { tile.requestKey?.let { requestQuickSettingsTile(context,it, onTileAdded = onTileAdded) } }, onSetupTile = {})
        }
    }
}

/** One card of the Chance group, on the secondary container. */
@Composable
private fun ChanceCard(onClick: () -> Unit, shape: RoundedCornerShape, modifier: Modifier = Modifier, centered: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Surface(onClick = onClick, modifier = modifier.fillMaxWidth().animateVisibility(), shape = shape, color = MaterialTheme.colorScheme.secondaryContainer) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start, content = content)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayScreenPreview() {
    MaterialTheme { Surface { PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues()) } }
}
