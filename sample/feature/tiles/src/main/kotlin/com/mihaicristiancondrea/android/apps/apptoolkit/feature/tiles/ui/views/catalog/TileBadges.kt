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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.badgeShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.lerp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitToolKind
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.backgroundShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.icon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.labelResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.statusColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

/**
 * A tool's badge, as the kit's `shapeBadge` draws it: the icon in the tool's accent, on the accent
 * mixed 26% into the highest surface container, cut from the tool's badge shape.
 */
@Composable
internal fun TileIconBadge(
    icon: ToolkitTileIcon,
    large: Boolean = false,
) {
    val colors = icon.iconColors()
    Box(
        modifier = Modifier
            .size(if (large) 64.dp else 44.dp)
            .clip(shape = icon.badgeShape())
            .background(color = lerp(MaterialTheme.colorScheme.surfaceContainerHighest, colors.container, 0.26f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.imageVector(),
            contentDescription = null,
            tint = colors.content,
            modifier = Modifier.size(if (large) 32.dp else 24.dp),
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
                imageVector = ToolSymbols.CheckCircle,
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
