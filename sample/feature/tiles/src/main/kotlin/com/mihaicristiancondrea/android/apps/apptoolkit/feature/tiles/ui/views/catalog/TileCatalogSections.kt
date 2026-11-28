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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTilesListItem
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads.QuickToolsNativeAdCard
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HiddenAdPreloaders(
    adItems: ImmutableList<ToolkitTilesListItem.Ad>,
    onEvent: (ToolkitTilesEvent) -> Unit,
) {
    Box(modifier = Modifier.size(size = DpSize.Zero)) {
        adItems.forEach { item ->
            QuickToolsNativeAdCard(
                adUnitId = item.adUnitId,
                onStatusChanged = { isLoaded ->
                    onEvent(ToolkitTilesEvent.AdStatusChanged(item.id, isLoaded))
                },
            )
        }
    }
}
