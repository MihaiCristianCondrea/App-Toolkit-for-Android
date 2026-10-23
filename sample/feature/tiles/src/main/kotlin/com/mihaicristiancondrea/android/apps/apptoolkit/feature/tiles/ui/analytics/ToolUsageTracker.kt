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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue

/**
 * Reports `tool_used` for one quick tool, at most once for each time its sheet is open.
 *
 * Opening a tool is already reported as `view_item` from the catalogue, and says nothing about
 * whether the person did anything with it. This is the step after: the first action that shows
 * real use, such as a flip, a roll, or a finished breath. Reporting it once per opening, instead of
 * on every action, keeps the event a count of sessions in which the tool was used, so a user
 * rolling the dice forty times does not outweigh forty users rolling once.
 *
 * The tool ViewModels outlive their sheet, so each one calls [endSession] when its sheet closes.
 *
 * @param toolId one of [ToolkitTileIds], so the value stays bounded and matches the catalogue.
 */
class ToolUsageTracker(
    private val telemetryRepository: TelemetryRepository,
    private val toolId: String,
) {
    private var hasReportedUse: Boolean = false

    /** Reports the tool as used, unless it already was since its sheet opened. */
    fun markUsed() {
        if (hasReportedUse) return
        hasReportedUse = true
        telemetryRepository.logEvent(
            AnalyticsEvent(
                name = AppGa4Contract.EventName.TOOL_USED,
                params = mapOf(AppGa4Contract.Param.TOOL_ID to AnalyticsValue.Str(toolId)),
            ),
        )
    }

    /** Starts over, so the next opening of the sheet can report use again. */
    fun endSession() {
        hasReportedUse = false
    }

    companion object {
        /**
         * How long a tool used by looking at it, such as the compass, must stay open to count as
         * used. Opening and closing it straight away is not use.
         */
        const val WATCHED_USE_DELAY_MS: Long = 5_000L
    }
}
