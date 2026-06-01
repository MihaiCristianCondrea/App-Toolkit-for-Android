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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTile
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileCategory
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileIcon
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesFilter
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

@Preview(showBackground = true)
@Composable
fun ToolkitTilesScreenPreview() {
    val mockState = ToolkitTilesUiState(
        categories = persistentListOf(
            ToolkitTileCategory(
                id = "sensors",
                titleResId = android.R.string.unknownName,
                icon = ToolkitTileIcon.Compass,
                tiles = persistentListOf(
                    ToolkitTile(
                        id = "level",
                        titleResId = android.R.string.unknownName,
                        summaryResId = android.R.string.unknownName,
                        icon = ToolkitTileIcon.Level,
                        status = ToolkitTileStatus.Available
                    ),
                    ToolkitTile(
                        id = "compass",
                        titleResId = android.R.string.unknownName,
                        summaryResId = android.R.string.unknownName,
                        icon = ToolkitTileIcon.Compass,
                        status = ToolkitTileStatus.NeedsSetup
                    )
                ),
                initiallyExpanded = true
            )
        ),
        selectedFilter = ToolkitTilesFilter.All,
        expandedCategoryIds = persistentSetOf("sensors")
    )

    MaterialTheme {
        Surface {
            ToolkitTilesScreen(
                state = mockState,
                paddingValues = PaddingValues(0.dp),
                onEvent = {}
            )
        }
    }
}
