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
internal object FaqAnalytics {
    const val SCREEN_NAME: String = "Help"
    val SCREEN = TrackedScreen(name = SCREEN_NAME, className = "FaqScreen")
}

internal fun faqSelectContentEvent(
    contentType: String,
    itemId: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent {
    return AnalyticsEvent(
        name = FaqEventNames.SELECT_CONTENT,
        params = buildMap {
            put(key = SettingsAnalytics.Params.SCREEN, value = AnalyticsValue.Str(value = FaqAnalytics.SCREEN_NAME))
            put(key = FaqParams.CONTENT_TYPE, value = AnalyticsValue.Str(value = contentType))
            put(key = FaqParams.ITEM_ID, value = AnalyticsValue.Str(value = itemId))
            putAll(from = params)
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
            FaqParams.FAQ_POSITION to AnalyticsValue.LongVal(value = position.toLong()),
            FaqParams.EXPANDED to AnalyticsValue.Bool(value = expanded),
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
            put(key = SettingsAnalytics.Params.SCREEN, value = AnalyticsValue.Str(value = FaqAnalytics.SCREEN_NAME))
            put(key = SettingsAnalytics.Params.ACTION_NAME, value = AnalyticsValue.Str(value = actionName))
            putAll(from = params)
        },
    )
}
