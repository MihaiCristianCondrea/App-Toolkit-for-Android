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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts

/** Stable GA4 schema used by sample-owned events. */
object AppGa4Contract {
    object EventName {
        const val APP_CARD_INTERACTION = "app_card_interaction"
        const val COMPONENTS_CLICK = "components_click"
        const val SELECT_CONTENT = "select_content"
        const val SEARCH = "search"
        const val SHARE = "share"
        const val VIEW_ITEM = "view_item"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val TUTORIAL_BEGIN = "tutorial_begin"
        const val TUTORIAL_COMPLETE = "tutorial_complete"
        const val UNLOCK_ACHIEVEMENT = "unlock_achievement"
    }

    object Param {
        const val SCREEN = "screen"
        const val COMPONENT = "component"
        const val VARIANT = "variant"
        const val SOURCE = "source"
        const val INTERACTION = "interaction"
        const val PACKAGE_NAME = "package_name"
        const val APP_NAME = "app_name"
        const val APP_CATEGORY_ID = "app_category_id"
        const val APP_CATEGORY_LABEL = "app_category_label"
        const val INTERACTION_CONTEXT = "interaction_context"
        const val CONTENT_TYPE = "content_type"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_LIST_ID = "item_list_id"
        const val ITEM_LIST_NAME = "item_list_name"
        const val SEARCH_TERM = "search_term"
        const val METHOD = "method"
        const val ACHIEVEMENT_ID = "achievement_id"
    }

    val forbiddenParamKeys: Set<String> = setOf(
        "device_id",
        "email",
        "exception",
        "file_name",
        "file_path",
        "message",
        "phone",
        "query",
        "stack_trace",
        "text",
        "uri",
        "user_id",
    )

    private val requiredParamsByEvent: Map<String, Set<String>> = mapOf(
        EventName.APP_CARD_INTERACTION to setOf(
            Param.SOURCE,
            Param.INTERACTION,
            Param.PACKAGE_NAME,
            Param.APP_NAME,
        ),
        EventName.COMPONENTS_CLICK to setOf(
            Param.SCREEN,
            Param.COMPONENT,
        ),
        EventName.SELECT_CONTENT to setOf(
            Param.CONTENT_TYPE,
            Param.ITEM_ID,
        ),
        EventName.SEARCH to setOf(
            Param.SEARCH_TERM,
        ),
        EventName.SHARE to setOf(
            Param.METHOD,
            Param.CONTENT_TYPE,
            Param.ITEM_ID,
        ),
        EventName.VIEW_ITEM to setOf(
            Param.ITEM_ID,
            Param.ITEM_NAME,
        ),
        EventName.VIEW_ITEM_LIST to emptySet(),
        EventName.TUTORIAL_BEGIN to emptySet(),
        EventName.TUTORIAL_COMPLETE to emptySet(),
        EventName.UNLOCK_ACHIEVEMENT to setOf(
            Param.ACHIEVEMENT_ID,
        ),
    )

    fun allEventNames(): Set<String> = requiredParamsByEvent.keys

    fun requiredParams(eventName: String): Set<String> = requiredParamsByEvent[eventName].orEmpty()
}

object AppGa4ContractValidator {
    private val eventNameRegex = Regex("^[A-Za-z][A-Za-z0-9_]{0,39}$")

    fun isValidEventName(name: String): Boolean = eventNameRegex.matches(name)

    fun missingRequiredParams(eventName: String, params: Set<String>): Set<String> =
        AppGa4Contract.requiredParams(eventName) - params

    fun forbiddenParams(params: Set<String>): Set<String> =
        params.filterTo(mutableSetOf()) { it in AppGa4Contract.forbiddenParamKeys }
}
