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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase

import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileCategory
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.ToolkitTilesRepository
import kotlinx.collections.immutable.toImmutableList

/**
 * Synchronizes the status of toolkit tiles by checking against the system's active Quick Settings tiles.
 */
class SyncToolkitTileStatusesUseCase(private val repository: ToolkitTilesRepository) {
    /**
     * Updates the [ToolkitTileStatus] for each tile in the provided [categories] list.
     *
     * @param categories The list of categories containing tiles to synchronize.
     * @return A new list of categories with updated tile statuses.
     */
    operator fun invoke(categories: List<ToolkitTileCategory>): List<ToolkitTileCategory> {
        val activeTiles = repository.getActiveQuickSettingsTiles()
        return categories.map { category ->
            category.copy(
                tiles = category.tiles.map { tile ->
                    val componentName =
                        tile.requestKey?.let { repository.getComponentFlattenedName(it) }
                    if (componentName != null && componentName in activeTiles) {
                        tile.copy(status = ToolkitTileStatus.Added)
                    } else {
                        tile
                    }
                }.toImmutableList()
            )
        }
    }
}
