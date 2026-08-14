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


package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.utils

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.ToolkitTilesFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun ImmutableList<ToolkitTileCategory>.filterFor(
    filter: ToolkitTilesFilter,
): List<ToolkitTileCategory> = when (filter) {
    ToolkitTilesFilter.All -> this
    ToolkitTilesFilter.Added -> filterByStatus(ToolkitTileStatus.Added)
    ToolkitTilesFilter.NeedsSetup -> filterByStatus(ToolkitTileStatus.NeedsSetup)
    ToolkitTilesFilter.Unsupported -> filterByStatus(ToolkitTileStatus.Unsupported)
}

internal fun ImmutableList<ToolkitTileCategory>.filterByStatus(
    status: ToolkitTileStatus,
): List<ToolkitTileCategory> = mapNotNull { category ->
    val tiles = category.tiles.filter { tile -> tile.status == status }
    if (tiles.isEmpty()) null else category.copy(tiles = tiles.toImmutableList())
}
