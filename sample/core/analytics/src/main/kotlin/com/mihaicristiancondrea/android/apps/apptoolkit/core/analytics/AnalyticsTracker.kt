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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent

interface AnalyticsTracker {
    fun logEvent(event: AnalyticsEvent)
    fun logScreenView(screenName: String)
}

class DefaultAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(event: AnalyticsEvent) {
        // In a real app, this would route to GA4, Firebase, or other providers.
        // For the sample, we can just log to logcat or use the library's stub.
    }

    override fun logScreenView(screenName: String) {
        // Log screen view event
    }
}
