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
