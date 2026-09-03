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
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Dehaze
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R as CoreUiR

/** Container and content colors used by a tile badge or status chip. */
internal data class StatusColors(
    val container: Color,
    val content: Color,
)

internal fun ToolkitTile.previewTextResId(): Int = when (id) {
    ToolkitTileIds.COIN_FLIP -> R.string.tile_preview_coin_result
    ToolkitTileIds.COMPASS -> R.string.tile_preview_compass_result
    ToolkitTileIds.BUBBLE_LEVEL -> R.string.tile_preview_level_result
    else -> R.string.tile_preview_default_result
}

internal fun ToolkitTileIcon.imageVector(): ImageVector = when (this) {
    ToolkitTileIcon.Level -> Icons.Outlined.Straighten
    ToolkitTileIcon.Compass -> Icons.Outlined.Explore
    ToolkitTileIcon.Coin -> Icons.Outlined.MonetizationOn
    ToolkitTileIcon.Dice -> Icons.Outlined.Casino
    ToolkitTileIcon.Counter -> Icons.Outlined.Dehaze
    ToolkitTileIcon.Caffeine -> Icons.Outlined.Timer
    ToolkitTileIcon.Sound -> Icons.Outlined.GraphicEq
    ToolkitTileIcon.Music -> Icons.Outlined.MusicNote
    ToolkitTileIcon.Breathing -> Icons.Outlined.FavoriteBorder
    ToolkitTileIcon.Sos -> Icons.Outlined.WarningAmber
    ToolkitTileIcon.Morse -> Icons.Outlined.MoreHoriz
    ToolkitTileIcon.FlashDimmer -> Icons.Outlined.FlashlightOn
    ToolkitTileIcon.Palette -> Icons.Outlined.Palette
}

internal fun ToolkitTileIcon.backgroundDrawableRes(): Int = when (this) {
    ToolkitTileIcon.Level,
    ToolkitTileIcon.Compass -> CoreUiR.drawable.background_8_sided_cookie

    ToolkitTileIcon.Caffeine,
    ToolkitTileIcon.Breathing -> CoreUiR.drawable.background_soft_burst

    ToolkitTileIcon.Sound,
    ToolkitTileIcon.FlashDimmer -> CoreUiR.drawable.background_flower

    ToolkitTileIcon.Coin,
    ToolkitTileIcon.Dice,
    ToolkitTileIcon.Counter,
    ToolkitTileIcon.Morse -> CoreUiR.drawable.background_12_sided_cookie

    ToolkitTileIcon.Sos -> CoreUiR.drawable.background_gem

    else -> CoreUiR.drawable.background_circle
}

internal fun ToolkitTileStatus.labelResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tiles_status_added
    ToolkitTileStatus.Available -> R.string.tiles_status_available
    ToolkitTileStatus.NeedsSetup -> R.string.tiles_status_needs_setup
    ToolkitTileStatus.Unsupported -> R.string.tiles_status_unsupported
}

internal fun ToolkitTileStatus.icon(): ImageVector = when (this) {
    ToolkitTileStatus.Added -> Icons.Outlined.CheckCircle
    ToolkitTileStatus.Available -> Icons.Outlined.Info
    ToolkitTileStatus.NeedsSetup -> Icons.Outlined.WarningAmber
    ToolkitTileStatus.Unsupported -> Icons.Outlined.Close
}

@Composable
internal fun ToolkitTileIcon.iconColors(): StatusColors {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    return when (this) {
        ToolkitTileIcon.Level,
        ToolkitTileIcon.Compass -> StatusColors(
            container = if (isDark) Color(0xFF006A60) else Color(0xFF00A091),
            content = if (isDark) Color(0xFF74DED1) else Color(0xFF006A60),
        )

        ToolkitTileIcon.Caffeine,
        ToolkitTileIcon.Breathing -> StatusColors(
            container = if (isDark) Color(0xFF8B4100) else Color(0xFFFF8B26),
            content = if (isDark) Color(0xFFFFB88E) else Color(0xFF8B4100),
        )

        ToolkitTileIcon.Coin,
        ToolkitTileIcon.Dice,
        ToolkitTileIcon.Counter,
        ToolkitTileIcon.Morse -> StatusColors(
            container = if (isDark) Color(0xFF6B5E00) else Color(0xFFE2C900),
            content = if (isDark) Color(0xFFFBE44D) else Color(0xFF6B5E00),
        )

        ToolkitTileIcon.Sound,
        ToolkitTileIcon.FlashDimmer -> StatusColors(
            container = if (isDark) Color(0xFF91005A) else Color(0xFFE2008E),
            content = if (isDark) Color(0xFFFFB0D3) else Color(0xFF91005A),
        )

        ToolkitTileIcon.Music -> StatusColors(
            container = if (isDark) Color(0xFF3F0091) else Color(0xFF6F00FF),
            content = if (isDark) Color(0xFFC8BFFF) else Color(0xFF3F0091),
        )

        ToolkitTileIcon.Palette -> StatusColors(
            container = if (isDark) Color(0xFF7A4E00) else Color(0xFFFFB84D),
            content = if (isDark) Color(0xFFFFDDA8) else Color(0xFF7A4E00),
        )

        ToolkitTileIcon.Sos -> StatusColors(
            container = if (isDark) Color(0xFFB10000) else Color(0xFFEE0000),
            content = if (isDark) Color(0xFFFFDAD6) else Color(0xFFB10000),
        )
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

    ToolkitTileStatus.NeedsSetup -> StatusColors(
        container = MaterialTheme.colorScheme.tertiaryContainer,
        content = MaterialTheme.colorScheme.onTertiaryContainer,
    )

    ToolkitTileStatus.Unsupported -> StatusColors(
        container = MaterialTheme.colorScheme.errorContainer,
        content = MaterialTheme.colorScheme.onErrorContainer,
    )
}
