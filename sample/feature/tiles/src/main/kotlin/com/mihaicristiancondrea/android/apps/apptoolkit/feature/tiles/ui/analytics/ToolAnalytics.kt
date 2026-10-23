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
 * Reports a finished Reaction Test round as the recommended `post_score` event.
 *
 * The score is the reaction time in milliseconds, so lower is better. `character` names the game,
 * which is how GA4 tells this score apart from any other scored feature added later.
 */
fun TelemetryRepository.logReactionScore(reactionTimeMs: Long) {
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
fun TelemetryRepository.logQuickSettingsTileRequest(tileId: String, outcome: String) {
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
