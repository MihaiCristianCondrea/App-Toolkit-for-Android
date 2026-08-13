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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

/**
 * Owns the Toolkit Tiles catalogue and the Quick Settings state each tile is shown with.
 *
 * The catalogue used to live in `GetToolkitTilesUseCase` and the status pass in
 * `SyncToolkitTileStatusesUseCase`. Neither was a use case: one was a hardcoded data set in the
 * domain layer, the other read this repositories and mapped over the result. Both are data-layer
 * work, and keeping them apart meant a caller could load the catalogue and forget to apply
 * statuses — which is what [tileCategories] now does for them.
 */
interface ToolkitTilesRepository {

    /** The curated catalogue, with each tile's current Quick Settings status already applied. */
    fun tileCategories(): Flow<ImmutableList<ToolkitTileCategory>>

    /**
     * Re-reads Quick Settings and returns [categories] with refreshed statuses.
     *
     * Used when returning from the system tile picker, where the catalogue has not changed but the
     * set of added tiles has.
     */
    fun withCurrentStatuses(categories: List<ToolkitTileCategory>): List<ToolkitTileCategory>
}
