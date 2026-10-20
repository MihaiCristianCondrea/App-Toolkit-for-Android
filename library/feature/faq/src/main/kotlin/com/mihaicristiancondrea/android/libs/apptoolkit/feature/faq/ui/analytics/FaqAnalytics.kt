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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

/**
 * Keeps the established `Help` analytics identity so the screen retains its GA4 history.
 */
internal const val FAQ_SCREEN_NAME: String = "Help"
private const val FAQ_SCREEN_CLASS: String = "FaqScreen"
internal val FAQ_SCREEN: TrackedScreen =
    TrackedScreen(name = FAQ_SCREEN_NAME, className = FAQ_SCREEN_CLASS)

internal object FaqPreferenceKeys {
    const val FEEDBACK: String = "feedback"
    const val REQUEST_FEATURE: String = "request_feature"
    const val LEAVE_REVIEW: String = "leave_review"
    const val FAQ_ITEM: String = "faq_item"
    const val SHOW_MORE_QUESTIONS: String = "show_more_questions"
    const val CONTACT_US: String = "contact_us"
}

internal object FaqActionNames {
    const val RETRY_LOAD: String = "retry_load"
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
}

internal fun faqPreferenceTapEvent(
    preferenceKey: String,
    faqId: String? = null,
    faqPosition: Int? = null,
    expanded: Boolean? = null,
): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(FAQ_SCREEN_NAME))
            put(SettingsAnalytics.Params.PREFERENCE_KEY, AnalyticsValue.Str(preferenceKey))
            faqId?.let { put(SettingsAnalytics.Params.FAQ_ID, AnalyticsValue.Str(it)) }
            faqPosition?.let { put(SettingsAnalytics.Params.FAQ_POSITION, AnalyticsValue.LongVal(it.toLong())) }
            expanded?.let { put(SettingsAnalytics.Params.EXPANDED, AnalyticsValue.Bool(it)) }
        },
    )
}

internal fun faqActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(FAQ_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
