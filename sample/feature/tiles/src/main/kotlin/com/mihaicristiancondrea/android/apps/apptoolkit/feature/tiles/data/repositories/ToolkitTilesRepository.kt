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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

/**
 * Owns the Toolkit Tiles catalogue and the Quick Settings state each tile is shown with.
 *
 * The catalogue used to live in `GetToolkitTilesUseCase` and the status pass in
 * `SyncToolkitTileStatusesUseCase`. Neither was a use case: one was a hardcoded data set in the
 * domain layer, the other read this repositories and mapped over the result. Both are data-layer
 * work, and keeping them apart meant a caller could load the catalogue and forget to apply
 * statuses, which is what [tileCategories] now does for them.
 */
interface ToolkitTilesRepository {

    /** The curated catalogue, with each tile's current Quick Settings status already applied. */
    fun tileCategories(): Flow<ImmutableList<ToolkitTileCategoryData>>

    /** Category IDs the user last left expanded, or catalogue defaults on first use. */
    val expandedCategoryIds: Flow<Set<String>>

    /** Persists the complete set of expanded category IDs. */
    suspend fun saveExpandedCategoryIds(categoryIds: Set<String>)

    /** Re-reads Quick Settings and returns the catalogue with refreshed statuses. */
    fun currentTileCategories(): ImmutableList<ToolkitTileCategoryData>
}
