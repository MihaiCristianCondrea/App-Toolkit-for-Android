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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Dehaze
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.StatusColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds

internal fun ToolkitTile.previewTextResId(): Int = when (id) {
    ToolkitTileIds.COIN_FLIP -> R.string.tile_preview_coin_result
    ToolkitTileIds.COMPASS -> R.string.tile_preview_compass_result
    ToolkitTileIds.BUBBLE_LEVEL -> R.string.tile_preview_level_result
    else -> R.string.tile_preview_default_result
}

internal fun ToolkitTileIcon.imageVector(): ImageVector = when (this) {
    ToolkitTileIcon.Wheel -> Icons.AutoMirrored.Outlined.RotateRight
    ToolkitTileIcon.Water -> Icons.Outlined.WaterDrop
    ToolkitTileIcon.Protractor -> Icons.Outlined.SquareFoot
    ToolkitTileIcon.BillSplitter -> Icons.AutoMirrored.Outlined.ReceiptLong
    ToolkitTileIcon.RandomNumber -> Icons.Outlined.Numbers
    ToolkitTileIcon.Level -> Icons.Outlined.Straighten
    ToolkitTileIcon.Compass -> Icons.Outlined.Explore
    ToolkitTileIcon.Coin -> Icons.Outlined.MonetizationOn
    ToolkitTileIcon.Dice -> Icons.Outlined.Casino
    ToolkitTileIcon.Counter -> Icons.Outlined.Dehaze
    ToolkitTileIcon.Breathing -> Icons.Outlined.FavoriteBorder
    ToolkitTileIcon.Sos -> Icons.Outlined.WarningAmber
    ToolkitTileIcon.Morse -> Icons.Outlined.MoreHoriz
    ToolkitTileIcon.FlashDimmer -> Icons.Outlined.FlashlightOn
    ToolkitTileIcon.Palette -> Icons.Outlined.Palette
    ToolkitTileIcon.Timer -> Icons.Outlined.Timer
    ToolkitTileIcon.Converter -> Icons.Outlined.SwapHoriz
    ToolkitTileIcon.Ruler -> Icons.Outlined.Straighten
    ToolkitTileIcon.ScreenLight -> Icons.Outlined.LightMode
}

/**
 * Material shape behind a tile icon, resolved for the active theme and shape choice.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ToolkitTileIcon.backgroundShape(): Shape = when (this) {
    ToolkitTileIcon.Compass -> com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.Cookie8Shape
    ToolkitTileIcon.Level -> com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.Cookie16Shape
    ToolkitTileIcon.Sos, ToolkitTileIcon.Morse -> MaterialShapes.Cookie7Sided.toShape()
    ToolkitTileIcon.FlashDimmer -> MaterialShapes.Burst.toShape()
    ToolkitTileIcon.Water, ToolkitTileIcon.Protractor, ToolkitTileIcon.Converter, ToolkitTileIcon.Timer -> MaterialShapes.Cookie9Sided.toShape()
    ToolkitTileIcon.Breathing, ToolkitTileIcon.BillSplitter, ToolkitTileIcon.Palette -> MaterialShapes.SoftBurst.toShape()
    ToolkitTileIcon.ScreenLight, ToolkitTileIcon.Counter -> MaterialShapes.Flower.toShape()
    ToolkitTileIcon.Coin, ToolkitTileIcon.Dice, ToolkitTileIcon.Ruler, ToolkitTileIcon.Wheel -> MaterialShapes.Cookie12Sided.toShape()
    ToolkitTileIcon.RandomNumber -> MaterialShapes.Gem.toShape()
}

internal fun ToolkitTileStatus.labelResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tiles_status_added
    ToolkitTileStatus.Available -> R.string.tiles_status_available
    ToolkitTileStatus.NotAdded -> R.string.tiles_status_not_added
    ToolkitTileStatus.Unsupported -> R.string.tiles_status_unsupported
}

internal fun ToolkitTileStatus.icon(): ImageVector = when (this) {
    ToolkitTileStatus.Added -> Icons.Outlined.CheckCircle
    ToolkitTileStatus.Available -> Icons.Outlined.Info
    ToolkitTileStatus.NotAdded -> Icons.Outlined.AddCircleOutline
    ToolkitTileStatus.Unsupported -> Icons.Outlined.Close
}

@Composable
internal fun ToolkitTileIcon.iconColors(): StatusColors {
    val dark = MaterialTheme.colorScheme.surface.luminance() < .5f
    return when (this) {
        ToolkitTileIcon.Level, ToolkitTileIcon.Compass, ToolkitTileIcon.Water -> StatusColors(
            container = if (dark) Color(0xFF006A60) else Color(0xFF00A091),
            content = if (dark) Color(0xFF74DED1) else Color(0xFF006A60))
        ToolkitTileIcon.Breathing -> StatusColors(
            container = if (dark) Color(0xFF8B4100) else Color(0xFFFF8B26),
            content = if (dark) Color(0xFFFFB88E) else Color(0xFF8B4100))
        ToolkitTileIcon.Sos, ToolkitTileIcon.Morse, ToolkitTileIcon.ScreenLight -> StatusColors(
            container = if (dark) Color(0xFF91005A) else Color(0xFFE2008E),
            content = if (dark) Color(0xFFFFB0D3) else Color(0xFF91005A))
        else -> StatusColors(
            container = if (dark) Color(0xFF6B5E00) else Color(0xFFE2C900),
            content = if (dark) Color(0xFFFBE44D) else Color(0xFF6B5E00))
    }
}

@Composable
internal fun ToolkitTileStatus.statusColors(): StatusColors = when (this) {
    ToolkitTileStatus.Added -> StatusColors(
        container = MaterialTheme.colorScheme.primaryContainer,
        content = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    ToolkitTileStatus.Available -> StatusColors(
        container = MaterialTheme.colorScheme.secondaryContainer,
        content = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    // Neutral on purpose: adding the Quick Settings tile is optional, so the chip must not read
    // like a warning or like work the tool is waiting for.
    ToolkitTileStatus.NotAdded -> StatusColors(
        container = MaterialTheme.colorScheme.surfaceContainerHighest,
        content = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    ToolkitTileStatus.Unsupported -> StatusColors(
        container = MaterialTheme.colorScheme.errorContainer,
        content = MaterialTheme.colorScheme.onErrorContainer,
    )
}
