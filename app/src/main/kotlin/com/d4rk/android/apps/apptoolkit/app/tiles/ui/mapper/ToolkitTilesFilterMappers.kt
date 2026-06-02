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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.WarningAmber
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.model.ToolkitTilesFilterItem
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Returns stable UI chip metadata for every Toolkit Tiles filter. */
fun ToolkitTilesFilter.Companion.items(): ImmutableList<ToolkitTilesFilterItem> = persistentListOf(
    ToolkitTilesFilterItem(ToolkitTilesFilter.All, R.string.tiles_filter_all, Icons.Outlined.GridView),
    ToolkitTilesFilterItem(ToolkitTilesFilter.Added, R.string.tiles_filter_added, Icons.Outlined.CheckCircle),
    ToolkitTilesFilterItem(ToolkitTilesFilter.NeedsSetup, R.string.tiles_filter_needs_setup, Icons.Outlined.WarningAmber),
    ToolkitTilesFilterItem(ToolkitTilesFilter.Unsupported, R.string.tiles_filter_unsupported, Icons.Outlined.Block),
)
