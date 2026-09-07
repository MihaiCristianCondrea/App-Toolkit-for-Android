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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models

import kotlinx.collections.immutable.ImmutableList

/**
 * How a tool relates to the Quick Settings panel.
 *
 * - [Added]: the tool's tile is already in the active panel.
 * - [Available]: the tool runs in the app. It either has no Quick Settings tile, or this device
 *   cannot be asked to add one.
 * - [NotAdded]: the tool has a tile that is not in the panel. Adding it is optional; the tool works
 *   in the app either way.
 * - [Unsupported]: Android exposes no public API for the action, so the entry is documented only.
 */
enum class ToolkitTileStatus { Added, Available, NotAdded, Unsupported }

enum class ToolkitToolKind { Quick, Expanded }

enum class ToolkitQuickTool { MaterialColors }

data class ToolkitTileCategoryData(
    val id: String,
    val tiles: ImmutableList<ToolkitTileData>,
    val initiallyExpanded: Boolean = false,
)

data class ToolkitTileData(
    val id: String,
    val status: ToolkitTileStatus,
    val kind: ToolkitToolKind = ToolkitToolKind.Expanded,
    val quickTool: ToolkitQuickTool? = null,
    val requestKey: String? = null,
)
