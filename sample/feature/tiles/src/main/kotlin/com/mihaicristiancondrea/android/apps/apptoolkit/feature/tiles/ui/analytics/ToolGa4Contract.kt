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

internal object ToolGa4Contract {
    object EventName {
        const val TOOL_OPENED = "tool_opened"
        const val TOOL_USED = "tool_used"
        const val TOOL_CYCLE_FINISHED = "tool_cycle_finished"
        const val QUICK_SETTINGS_TILE_REQUEST = "quick_settings_tile_request"
        const val POST_SCORE = "post_score"
    }
    object Param {
        const val TOOL_ID = "tool_id"
        const val WORKSPACE = "workspace"
        const val OUTCOME = "outcome"
        const val TILE_ID = "tile_id"
        const val SCORE = "score"
        const val CHARACTER = "character"
    }
    object ToolCycleOutcome {
        const val COMPLETED = "completed"
        const val CANCELLED = "cancelled"
        const val FAILED = "failed"
        val all = setOf(COMPLETED, CANCELLED, FAILED)
    }
    object TileRequestOutcome {
        const val ADDED = "added"
        const val ALREADY_ADDED = "already_added"
        const val DECLINED = "declined"
        const val FAILED = "failed"
        const val UNSUPPORTED = "unsupported"
        const val UNAVAILABLE = "unavailable"
        val all = setOf(ADDED, ALREADY_ADDED, DECLINED, FAILED, UNSUPPORTED, UNAVAILABLE)
    }
    val requiredParamsByEvent = mapOf(
        EventName.TOOL_OPENED to setOf(Param.TOOL_ID, Param.WORKSPACE),
        EventName.TOOL_USED to setOf(Param.TOOL_ID),
        EventName.TOOL_CYCLE_FINISHED to setOf(Param.TOOL_ID, Param.WORKSPACE, Param.OUTCOME),
        EventName.QUICK_SETTINGS_TILE_REQUEST to setOf(Param.TILE_ID, Param.OUTCOME),
        EventName.POST_SCORE to setOf(Param.SCORE, Param.CHARACTER),
    )
    fun requiredParams(eventName: String): Set<String> = requiredParamsByEvent[eventName].orEmpty()
}
