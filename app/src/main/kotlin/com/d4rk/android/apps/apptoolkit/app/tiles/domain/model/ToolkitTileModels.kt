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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/** Stable identifier for the tile artwork rendered by the UI layer. */
enum class ToolkitTileIcon {
    Level,
    Compass,
    Lux,
    Network,
    Temperature,
    Coin,
    Dice,
    Counter,
    Clipboard,
    Battery,
    Memory,
    Caffeine,
    Sound,
    Volume,
    Screenshot,
    Lock,
    Power,
    Music,
    Breathing,
    Sos,
}

/** Installation/setup state shown by the toolkit tile catalog. */
enum class ToolkitTileStatus {
    Added,
    Available,
    NeedsSetup,
    Unsupported,
}

/** Groups related Quick Settings tile ideas in the Tiles screen. */
@Immutable
data class ToolkitTileCategory(
    val id: String,
    @StringRes val titleResId: Int,
    val icon: ToolkitTileIcon,
    val tiles: ImmutableList<ToolkitTile>,
    val initiallyExpanded: Boolean = false,
)

/**
 * Describes one Quick Settings tile shown in the catalog.
 *
 * Text is stored as resource IDs so UI can resolve localized copy at render time, and [requestKey]
 * keeps platform add-tile wiring outside of the domain layer.
 */
@Immutable
data class ToolkitTile(
    val id: String,
    @StringRes val titleResId: Int,
    @StringRes val summaryResId: Int,
    val icon: ToolkitTileIcon,
    val status: ToolkitTileStatus,
    val requestKey: String? = null,
)
