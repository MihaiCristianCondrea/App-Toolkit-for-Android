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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue

/** Only catalogue identifiers may enter product analytics, never tool inputs or results. */
internal fun toolWorkspace(toolId: String): String = when (toolId) {
    ToolkitTileIds.COIN_FLIP, ToolkitTileIds.DICE_ROLL,
    ToolkitTileIds.RANDOM_NUMBER, ToolkitTileIds.DECISION_WHEEL -> "play"
    ToolkitTileIds.RULER, ToolkitTileIds.PROTRACTOR, ToolkitTileIds.UNIT_CONVERTER,
    ToolkitTileIds.BILL_SPLITTER, ToolkitTileIds.COUNTER, ToolkitTileIds.REACTION_TEST -> "measure"
    ToolkitTileIds.COMPASS, ToolkitTileIds.BUBBLE_LEVEL, ToolkitTileIds.FLASH_DIMMER,
    ToolkitTileIds.MORSE, ToolkitTileIds.SOS, ToolkitTileIds.WATER_EJECT,
    ToolkitTileIds.BREATHING, ToolkitTileIds.MATERIAL_COLORS -> "quick_tools"
    else -> error("Unknown catalogue tool")
}

internal fun toolOpenedEvent(toolId: String): AnalyticsEvent = AnalyticsEvent(
    name = AppGa4Contract.EventName.TOOL_OPENED,
    params = toolJourneyParams(toolId),
)

internal fun toolCycleFinishedEvent(toolId: String, outcome: String): AnalyticsEvent {
    require(outcome in AppGa4Contract.ToolCycleOutcome.all)
    return AnalyticsEvent(
        name = AppGa4Contract.EventName.TOOL_CYCLE_FINISHED,
        params = toolJourneyParams(toolId) + (AppGa4Contract.Param.OUTCOME to AnalyticsValue.Str(outcome)),
    )
}

internal fun toolJourneyParams(toolId: String): Map<String, AnalyticsValue> = mapOf(
    AppGa4Contract.Param.TOOL_ID to AnalyticsValue.Str(toolId),
    AppGa4Contract.Param.WORKSPACE to AnalyticsValue.Str(toolWorkspace(toolId)),
)
