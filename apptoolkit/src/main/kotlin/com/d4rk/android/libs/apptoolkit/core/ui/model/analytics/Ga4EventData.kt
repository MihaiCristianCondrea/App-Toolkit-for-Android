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

package com.d4rk.android.libs.apptoolkit.core.ui.model.analytics

import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue

/**
 * UI-friendly GA4 event payload that can be reused by reusable App Toolkit components.
 *
 * @param name The GA4 event name to log.
 * @param params Optional event parameters using the analytics domain value types.
 */
data class Ga4EventData(
    val name: String,
    val params: Map<String, AnalyticsValue> = emptyMap(),
) {
    /**
     * Converts this UI payload into a domain [AnalyticsEvent] for Firebase logging.
     */
    fun toAnalyticsEvent(): AnalyticsEvent = AnalyticsEvent(
        name = name,
        params = params,
    )
}
