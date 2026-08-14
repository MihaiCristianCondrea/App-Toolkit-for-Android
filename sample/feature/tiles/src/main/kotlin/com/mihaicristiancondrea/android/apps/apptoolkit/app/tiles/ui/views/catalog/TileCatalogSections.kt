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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.mappers.items
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models.ToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.ads.QuickToolsNativeAdCard
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HiddenAdPreloaders(
    adItems: ImmutableList<ToolkitTilesListItem.Ad>,
    onEvent: (ToolkitTilesEvent) -> Unit,
) {
    Box(modifier = Modifier.size(0.dp)) {
        adItems.forEach { item ->
            QuickToolsNativeAdCard(
                adUnitId = item.adUnitId,
                position = GroupedItemPosition.MIDDLE,
                onStatusChanged = { isLoaded ->
                    onEvent(ToolkitTilesEvent.AdStatusChanged(item.id, isLoaded))
                },
            )
        }
    }
}

@Composable
internal fun TilesFilters(
    categories: ImmutableList<ToolkitTileCategory>,
    selectedFilter: ToolkitTilesFilter,
    onFilterSelected: (ToolkitTilesFilter) -> Unit,
) {
    val filters = remember(categories) {
        val allTiles = categories.flatMap { it.tiles }
        val statuses = allTiles.map { it.status }.toSet()

        ToolkitTilesFilter.items().filter { item ->
            when (item.filter) {
                ToolkitTilesFilter.All -> true
                ToolkitTilesFilter.Added -> statuses.contains(ToolkitTileStatus.Added)
                ToolkitTilesFilter.NeedsSetup -> statuses.contains(ToolkitTileStatus.NeedsSetup)
                ToolkitTilesFilter.Unsupported -> statuses.contains(ToolkitTileStatus.Unsupported)
            }
        }.toImmutableList()
    }

    if (filters.size > 1) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        ) {
            filters.forEach { item ->
                FilterChip(
                    selected = selectedFilter == item.filter,
                    onClick = { onFilterSelected(item.filter) },
                    label = { Text(text = stringResource(id = item.labelResId)) },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(SizeConstants.ButtonIconSize),
                        )
                    },
                )
            }
        }
    }
}

@Composable
internal fun TileCategorySection(
    category: ToolkitTileCategory,
    position: GroupedItemPosition,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    selectedFilter: ToolkitTilesFilter = ToolkitTilesFilter.All,
    onToggle: () -> Unit,
    onPreviewTile: (ToolkitTile) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .groupedCorners(
                position = position,
                outerRadius = SizeConstants.ExtraLargeIncreasedSize,
            ),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.padding(SizeConstants.MediumSize)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TileIconBadge(icon = category.icon)
                Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                Text(
                    text = stringResource(id = category.titleResId),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = pluralStringResource(
                        id = R.plurals.tiles_count_format,
                        count = category.tiles.size,
                        category.tiles.size,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = stringResource(
                            id = if (expanded) R.string.tiles_collapse_category else R.string.tiles_expand_category,
                        ),
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                    Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))
                    category.tiles.forEachIndexed { index, tile ->
                        ToolkitTileCard(
                            tile = tile,
                            position = groupedItemPosition(index, category.tiles.size),
                            modifier = Modifier.animateVisibility(index = index),
                            key = "${selectedFilter}_${tile.id}",
                            onPreviewTile = { onPreviewTile(tile) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ToolkitTileCard(
    tile: ToolkitTile,
    position: GroupedItemPosition,
    modifier: Modifier = Modifier,
    key: Any? = null,
    onPreviewTile: () -> Unit,
) {
    key(key) {
        Card(
            onClick = onPreviewTile,
            modifier = modifier
                .fillMaxWidth()
                .groupedCorners(
                    position = position,
                    outerRadius = SizeConstants.LargeExpandedSize,
                ),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.LargeSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
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
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(id = tile.summaryResId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                ToolkitToolChips(tile = tile)
            }
        }
    }
}
