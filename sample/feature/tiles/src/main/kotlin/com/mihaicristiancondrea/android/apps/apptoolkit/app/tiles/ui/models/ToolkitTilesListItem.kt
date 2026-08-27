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


package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

internal data class PositionedToolkitTilesListItem(
    val item: ToolkitTilesListItem,
    val position: GroupedItemPosition,
)

internal val ToolkitTilesListItem.stableKey: String
    get() = when (this) {
        is ToolkitTilesListItem.Category -> category.id
        is ToolkitTilesListItem.Ad -> id
    }

internal fun ToolkitTilesListItem.isVisible(
    loadedAdIds: Set<String>,
    showAds: Boolean,
): Boolean = when (this) {
    is ToolkitTilesListItem.Category -> true
    is ToolkitTilesListItem.Ad -> showAds && id in loadedAdIds
}

internal sealed class ToolkitTilesListItem {
    data class Category(val category: ToolkitTileCategory) : ToolkitTilesListItem()
    data class Ad(val id: String, val adUnitId: String) : ToolkitTilesListItem()
}
