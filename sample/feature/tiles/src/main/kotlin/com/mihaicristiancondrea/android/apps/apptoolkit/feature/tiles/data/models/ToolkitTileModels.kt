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

enum class ToolkitTileStatus { Added, Available, NeedsSetup, Unsupported }

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
