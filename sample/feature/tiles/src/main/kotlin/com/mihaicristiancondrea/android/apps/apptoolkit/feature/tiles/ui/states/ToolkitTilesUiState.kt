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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf

/**
 * State rendered by the Toolkit Tiles screen.
 *
 * @property categories The tile catalogue, with each tile's Quick Settings status.
 * @property pendingTileRequest A tile the screen should ask Android to add, until it reports the
 * request launched.
 */
@Immutable
data class ToolkitTilesUiState(
    val categories: Loadable<ImmutableList<ToolkitTileCategory>> = Loadable.Loading,
    val selectedFilter: ToolkitTilesFilter = ToolkitTilesFilter.All,
    val expandedCategoryIds: PersistentSet<String> = persistentSetOf(),
    val loadedAdIds: PersistentSet<String> = persistentSetOf(),
    val pendingTileRequest: String? = null,
)

enum class ToolkitTilesFilter {
    All,
    Added,

    /** Tools whose Quick Settings tile is not in the panel. Adding it is optional. */
    NotAdded,
    Unsupported;

    /**
     * The explicit companion owns the filter-mapping extension.
     */
    companion object
}
