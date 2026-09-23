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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue

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
    private val firebaseController: FirebaseController,
    private val toolId: String,
) {
    private var hasReportedUse: Boolean = false

    /** Reports the tool as used, unless it already was since its sheet opened. */
    fun markUsed() {
        if (hasReportedUse) return
        hasReportedUse = true
        firebaseController.logEvent(
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
}

/**
 * Reports a finished Reaction Test round as the recommended `post_score` event.
 *
 * The score is the reaction time in milliseconds, so lower is better. `character` names the game,
 * which is how GA4 tells this score apart from any other scored feature added later.
 */
fun FirebaseController.logReactionScore(reactionTimeMs: Long) {
    logEvent(
        AnalyticsEvent(
            name = AppGa4Contract.EventName.POST_SCORE,
            params = mapOf(
                AppGa4Contract.Param.SCORE to AnalyticsValue.LongVal(reactionTimeMs),
                AppGa4Contract.Param.CHARACTER to AnalyticsValue.Str(ToolkitTileIds.REACTION_TEST),
            ),
        ),
    )
}

/**
 * Reports how Android answered a request to add a quick tool to Quick Settings.
 *
 * @param outcome one of [AppGa4Contract.TileRequestOutcome].
 */
fun FirebaseController.logQuickSettingsTileRequest(tileId: String, outcome: String) {
    logEvent(
        AnalyticsEvent(
            name = AppGa4Contract.EventName.QUICK_SETTINGS_TILE_REQUEST,
            params = mapOf(
                AppGa4Contract.Param.TILE_ID to AnalyticsValue.Str(tileId),
                AppGa4Contract.Param.OUTCOME to AnalyticsValue.Str(outcome),
            ),
        ),
    )
}
