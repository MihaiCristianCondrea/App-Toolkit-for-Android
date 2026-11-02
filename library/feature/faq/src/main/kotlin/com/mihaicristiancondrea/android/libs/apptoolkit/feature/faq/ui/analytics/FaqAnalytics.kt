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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics

/**
 * Keeps the established `Help` analytics identity so the screen retains its GA4 history.
 */
internal const val FAQ_SCREEN_NAME: String = "Help"
private const val FAQ_SCREEN_CLASS: String = "FaqScreen"
internal val FAQ_SCREEN: TrackedScreen =
    TrackedScreen(name = FAQ_SCREEN_NAME, className = FAQ_SCREEN_CLASS)

internal object FaqEventNames {
    /** Recommended GA4 event for choosing an FAQ question or feedback sheet option. */
    const val SELECT_CONTENT: String = "select_content"

    /** Custom action event for discrete buttons and menu clicks on the help page. */
    const val ACTION: String = SettingsAnalytics.Events.ACTION
}

internal object FaqContentTypes {
    const val FAQ_ITEM: String = "faq_item"
    const val FEEDBACK_OPTION: String = "feedback_option"
    const val SUPPORT: String = "support"
}

internal object FaqItemIds {
    const val REQUEST_FEATURE: String = "request_feature"
    const val LEAVE_REVIEW: String = "leave_review"
    const val CONTACT_US: String = "contact_us"
}

internal object FaqActionNames {
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
    const val FEEDBACK_SHEET_DISMISSED: String = "feedback_sheet_dismissed"
    const val SHOW_MORE_QUESTIONS: String = "show_more_questions"
    const val RETRY_LOAD: String = "retry_load"
    const val STORE_LISTING_OPENED: String = "store_listing_opened"
    const val MENU_PLAY_STORE: String = "menu_play_store"
    const val MENU_VERSION_INFO: String = "menu_version_info"
    const val MENU_BETA_PROGRAM: String = "menu_beta_program"
    const val MENU_TERMS_OF_SERVICE: String = "menu_terms_of_service"
    const val MENU_PRIVACY_POLICY: String = "menu_privacy_policy"
    const val MENU_LICENSES: String = "menu_licenses"
}

internal object FaqParams {
    const val CONTENT_TYPE: String = "content_type"
    const val ITEM_ID: String = "item_id"
    const val FAQ_POSITION: String = SettingsAnalytics.Params.FAQ_POSITION
    const val EXPANDED: String = SettingsAnalytics.Params.EXPANDED
}

internal fun faqSelectContentEvent(
    contentType: String,
    itemId: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent {
    return AnalyticsEvent(
        name = FaqEventNames.SELECT_CONTENT,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(FAQ_SCREEN_NAME))
            put(FaqParams.CONTENT_TYPE, AnalyticsValue.Str(contentType))
            put(FaqParams.ITEM_ID, AnalyticsValue.Str(itemId))
            putAll(params)
        },
    )
}

internal fun faqQuestionSelectEvent(
    faqId: String,
    position: Int,
    expanded: Boolean,
): AnalyticsEvent {
    return faqSelectContentEvent(
        contentType = FaqContentTypes.FAQ_ITEM,
        itemId = faqId,
        params = mapOf(
            FaqParams.FAQ_POSITION to AnalyticsValue.LongVal(position.toLong()),
            FaqParams.EXPANDED to AnalyticsValue.Bool(expanded),
        ),
    )
}

internal fun faqActionEvent(
    actionName: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent {
    return AnalyticsEvent(
        name = FaqEventNames.ACTION,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(FAQ_SCREEN_NAME))
            put(SettingsAnalytics.Params.ACTION_NAME, AnalyticsValue.Str(actionName))
            putAll(params)
        },
    )
}
