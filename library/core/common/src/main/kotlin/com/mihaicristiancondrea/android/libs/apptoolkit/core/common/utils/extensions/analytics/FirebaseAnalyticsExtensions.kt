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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue

/**
 * Standard GA4 recommended event helpers on [FirebaseController].
 *
 * Provides typed extensions for Google Analytics recommended events and parameters:
 * - tutorial_begin / tutorial_complete
 * - search
 * - select_content
 * - share
 * - view_item / view_item_list
 * - unlock_achievement
 */

/** Logs standard GA4 `tutorial_begin` event. */
fun FirebaseController.logTutorialBegin() {
    logEvent(AnalyticsEvent(name = "tutorial_begin"))
}

/** Logs standard GA4 `tutorial_complete` event. */
fun FirebaseController.logTutorialComplete() {
    logEvent(AnalyticsEvent(name = "tutorial_complete"))
}

/** Logs standard GA4 `search` event with parameter `search_term`. */
fun FirebaseController.logSearch(searchTerm: String) {
    if (searchTerm.isBlank()) return
    logEvent(
        AnalyticsEvent(
            name = "search",
            params = mapOf("search_term" to AnalyticsValue.Str(searchTerm)),
        )
    )
}

/** Logs standard GA4 `select_content` event with parameters `content_type` and `item_id`. */
fun FirebaseController.logSelectContent(contentType: String, itemId: String) {
    logEvent(
        AnalyticsEvent(
            name = "select_content",
            params = mapOf(
                "content_type" to AnalyticsValue.Str(contentType),
                "item_id" to AnalyticsValue.Str(itemId),
            ),
        )
    )
}

/** Logs standard GA4 `share` event with parameters `method`, `content_type`, and `item_id`. */
fun FirebaseController.logShare(
    method: String,
    contentType: String,
    itemId: String,
) {
    logEvent(
        AnalyticsEvent(
            name = "share",
            params = mapOf(
                "method" to AnalyticsValue.Str(method),
                "content_type" to AnalyticsValue.Str(contentType),
                "item_id" to AnalyticsValue.Str(itemId),
            ),
        )
    )
}

/** Logs standard GA4 `view_item` event with parameters `item_id`, `item_name`, and optional `item_category`. */
fun FirebaseController.logViewItem(
    itemId: String,
    itemName: String,
    itemCategory: String? = null,
) {
    logEvent(
        AnalyticsEvent(
            name = "view_item",
            params = buildMap {
                put("item_id", AnalyticsValue.Str(itemId))
                put("item_name", AnalyticsValue.Str(itemName))
                if (!itemCategory.isNullOrBlank()) {
                    put("item_category", AnalyticsValue.Str(itemCategory))
                }
            },
        )
    )
}

/** Logs standard GA4 `view_item_list` event with optional parameters `item_list_id` and `item_list_name`. */
fun FirebaseController.logViewItemList(
    itemListId: String? = null,
    itemListName: String? = null,
) {
    logEvent(
        AnalyticsEvent(
            name = "view_item_list",
            params = buildMap {
                if (!itemListId.isNullOrBlank()) {
                    put("item_list_id", AnalyticsValue.Str(itemListId))
                }
                if (!itemListName.isNullOrBlank()) {
                    put("item_list_name", AnalyticsValue.Str(itemListName))
                }
            },
        )
    )
}

/** Logs standard GA4 `unlock_achievement` event with parameter `achievement_id`. */
fun FirebaseController.logUnlockAchievement(achievementId: String) {
    logEvent(
        AnalyticsEvent(
            name = "unlock_achievement",
            params = mapOf("achievement_id" to AnalyticsValue.Str(achievementId)),
        )
    )
}
