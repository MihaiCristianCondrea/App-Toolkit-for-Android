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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitToolKind
import kotlinx.collections.immutable.ImmutableList

enum class ToolkitTileIcon {
    Level, Compass, Coin, Dice, Counter, Caffeine, Sound, Music, Breathing, Sos, Morse,
    FlashDimmer, Palette,
}

@Immutable
data class ToolkitTileCategory(
    val id: String,
    @StringRes val titleResId: Int,
    val icon: ToolkitTileIcon,
    val tiles: ImmutableList<ToolkitTile>,
)

@Immutable
data class ToolkitTile(
    val id: String,
    @StringRes val titleResId: Int,
    @StringRes val summaryResId: Int,
    val icon: ToolkitTileIcon,
    val status: ToolkitTileStatus,
    val kind: ToolkitToolKind = ToolkitToolKind.Expanded,
    val quickTool: ToolkitQuickTool? = null,
    val requestKey: String? = null,
)
