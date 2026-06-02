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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.using
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentPasteOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTile
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlin.random.Random

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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
private fun ToolInteractiveContent(tile: ToolkitTile) {
    when (tile.id) {
        "coin_flip" -> CoinFlipTool()
        "dice_roll" -> DiceRollTool()
        "counter" -> CounterTool()
        "clipboard" -> ClipboardTool()
        "battery" -> BatteryTool()
        else -> GenericToolPreview(tile = tile)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CoinFlipTool() {
    var resultResId by remember { mutableIntStateOf(R.string.tool_coin_flip_waiting) }
    val scale by animateFloatAsState(
        targetValue = if (resultResId == R.string.tool_coin_flip_waiting) 0.94f else 1f,
        animationSpec = spring(),
        label = "coin-scale",
    )

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        ResultPill(
            modifier = Modifier.scale(scale),
            label = stringResource(id = resultResId),
        )
        Button(onClick = {
            resultResId = if (Random.nextBoolean()) R.string.tile_service_heads else R.string.tile_service_tails
        }) {
            Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_coin_flip_action))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DiceRollTool() {
    var value by remember { mutableIntStateOf(1) }
    var hasRolled by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        AnimatedContent(
            targetState = if (hasRolled) value.toString() else stringResource(id = R.string.tool_dice_roll_waiting),
            transitionSpec = {
                (scaleIn() + fadeIn()).togetherWith(scaleOut() + fadeOut()).using(SizeTransform(clip = false))
            },
            label = "dice-result",
        ) { label ->
            ResultPill(label = label)
        }
        Button(onClick = {
            value = Random.nextInt(from = 1, until = 7)
            hasRolled = true
        }) {
            Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_dice_roll_action))
        }
    }
}

@Composable
private fun CounterTool() {
    var count by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        ResultPill(label = count.toString())
        Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
            Button(onClick = { count += 1 }) {
                Text(text = stringResource(id = R.string.tool_counter_increment))
            }
            OutlinedButton(onClick = { count = 0 }) {
                Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
                Text(text = stringResource(id = R.string.tool_counter_reset))
            }
        }
    }
}

@Composable
private fun ClipboardTool() {
    val context = LocalContext.current
    var cleared by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        AnimatedVisibility(visible = cleared) {
            ResultPill(label = stringResource(id = R.string.tool_clipboard_cleared))
        }
        Button(onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(EMPTY_CLIP_LABEL, EMPTY_CLIP_TEXT))
            cleared = true
        }) {
            Icon(imageVector = Icons.Outlined.ContentPasteOff, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_clipboard_clear_action))
        }
    }
}

@Composable
private fun BatteryTool() {
    val context = LocalContext.current
    var percent by remember { mutableIntStateOf(context.currentBatteryPercent()) }
    val safePercent = percent.coerceAtLeast(0)

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.BatteryChargingFull,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = if (percent >= 0) {
                    stringResource(id = R.string.tile_service_percent, safePercent)
                } else {
                    stringResource(id = R.string.tool_battery_unknown)
                },
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        LinearProgressIndicator(
            progress = { if (percent >= 0) safePercent / 100f else 0f },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedButton(onClick = { percent = context.currentBatteryPercent() }) {
            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_battery_refresh))
        }
    }
}

@Composable
private fun GenericToolPreview(tile: ToolkitTile) {
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        ResultPill(label = stringResource(id = tile.previewTextResId()))
        Text(
            text = if (tile.status == ToolkitTileStatus.Available) {
                stringResource(id = R.string.tool_generic_available_summary)
            } else {
                stringResource(id = R.string.tool_generic_setup_summary)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ResultPill(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
            )
            .padding(
                horizontal = SizeConstants.ExtraLargeSize,
                vertical = SizeConstants.MediumSize,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
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

private fun Context.currentBatteryPercent(): Int {
    val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, UNKNOWN_LEVEL) ?: UNKNOWN_LEVEL
    val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, DEFAULT_SCALE) ?: DEFAULT_SCALE
    return if (level >= 0 && scale > 0) level * 100 / scale else UNKNOWN_LEVEL
}

private const val EMPTY_CLIP_LABEL: String = "empty"
private const val EMPTY_CLIP_TEXT: String = ""
private const val UNKNOWN_LEVEL: Int = -1
private const val DEFAULT_SCALE: Int = 100
