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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import kotlinx.collections.immutable.toImmutableList

internal val MeasureTools = listOf(
    ToolkitTileIds.RULER, ToolkitTileIds.PROTRACTOR, ToolkitTileIds.UNIT_CONVERTER,
    ToolkitTileIds.BILL_SPLITTER, ToolkitTileIds.COUNTER, ToolkitTileIds.REACTION_TEST,
).map { ToolkitTileData(id = it, status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available, requestKey = if (it == ToolkitTileIds.COUNTER) it else null).toUiModel() }.toImmutableList()

internal val PlayTools = listOf(
    ToolkitTileIds.DECISION_WHEEL, ToolkitTileIds.RANDOM_NUMBER, ToolkitTileIds.COIN_FLIP, ToolkitTileIds.DICE_ROLL,
).map { ToolkitTileData(id = it, status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available, requestKey = if (it == ToolkitTileIds.COIN_FLIP || it == ToolkitTileIds.DICE_ROLL) it else null).toUiModel() }.toImmutableList()
