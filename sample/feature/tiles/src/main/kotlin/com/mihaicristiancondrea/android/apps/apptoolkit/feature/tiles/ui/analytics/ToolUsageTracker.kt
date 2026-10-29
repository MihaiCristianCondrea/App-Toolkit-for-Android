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

/**
 * Reports `tool_used` once per use-session, at the first meaningful action or watched interval.
 *
 * ViewModels call [endSession] when their existing tool lifecycle ends. Shared overview controls
 * can begin use before a sheet opens; repeated taps do not inflate the number of use-sessions.
 *
 * @param toolId one of [ToolkitTileIds], so identifiers stay bounded and match the catalogue.
 */
class ToolUsageTracker(
    private val telemetryRepository: TelemetryRepository,
    private val toolId: String,
) {
    private var hasReportedUse: Boolean = false

    /** Reports the first use since the last [endSession]. */
    fun markUsed() {
        if (hasReportedUse) return
        hasReportedUse = true
        telemetryRepository.logEvent(
            AnalyticsEvent(
                name = AppGa4Contract.EventName.TOOL_USED,
                params = toolJourneyParams(toolId),
            ),
        )
    }

    /** Starts a new use-session without reporting an event. */
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
