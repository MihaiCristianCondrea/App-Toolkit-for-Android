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


package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitToolKind
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.backgroundDrawableRes
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.icon
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.labelResId
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.statusColors
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

@Composable
internal fun TileIconBadge(
    icon: ToolkitTileIcon,
    large: Boolean = false,
) {
    val colors = icon.iconColors()
    val size =
        if (large) SizeConstants.LauncherIconSize + SizeConstants.SmallSize else SizeConstants.FortyFourSize
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = icon.backgroundDrawableRes()),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(colors.container.copy(alpha = 0.15f)),
        )
        Icon(
            imageVector = icon.imageVector(),
            contentDescription = null,
            tint = colors.content,
        )
    }
}

@Composable
internal fun ToolkitToolChips(tile: ToolkitTile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ToolTypeChip(kind = tile.kind)
        if (tile.requestKey != null) {
            TileAvailabilityChip()
        }
        if (tile.status != ToolkitTileStatus.Available) {
            TileStatusChip(status = tile.status)
        }
    }
}

@Composable
internal fun ToolTypeChip(kind: ToolkitToolKind) {
    val labelResId = when (kind) {
        ToolkitToolKind.Quick -> R.string.tiles_chip_quick_tool
        ToolkitToolKind.Expanded -> R.string.tiles_chip_expanded_tool
    }
    AssistChip(
        onClick = {},
        label = { Text(text = stringResource(id = labelResId)) },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        border = null,
    )
}

@Composable
internal fun TileAvailabilityChip() {
    AssistChip(
        onClick = {},
        label = { Text(text = stringResource(id = R.string.tiles_chip_tile)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(SizeConstants.ButtonIconSize),
            )
        },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = null,
    )
}

@Composable
internal fun TileStatusChip(
    status: ToolkitTileStatus,
    onClick: () -> Unit = {},
) {
    val colors = status.statusColors()
    AssistChip(
        onClick = onClick,
        label = { Text(text = stringResource(id = status.labelResId())) },
        leadingIcon = {
            Icon(
                imageVector = status.icon(),
                contentDescription = null,
                modifier = Modifier.size(SizeConstants.ButtonIconSize),
            )
        },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            containerColor = colors.container,
            labelColor = colors.content,
            leadingIconContentColor = colors.content,
        ),
        border = null,
    )
}
