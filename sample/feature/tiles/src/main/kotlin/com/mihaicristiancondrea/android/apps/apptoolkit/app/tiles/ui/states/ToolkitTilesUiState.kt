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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

/** State rendered by the Toolkit Tiles screen. */
@Immutable
data class ToolkitTilesUiState(
    val categories: ImmutableList<ToolkitTileCategory> = persistentListOf(),
    val selectedFilter: ToolkitTilesFilter = ToolkitTilesFilter.All,
    val expandedCategoryIds: PersistentSet<String> = persistentSetOf(),
    val loadedAdIds: PersistentSet<String> = persistentSetOf(),
)

enum class ToolkitTilesFilter {
    All,
    Added,
    NeedsSetup,
    Unsupported;

    // Not redundant despite reading as an empty body: `ToolkitTilesFilterMappers.items()` is
    // declared on ToolkitTilesFilter.Companion, and an enum has no implicit companion to extend.
    companion object
}
