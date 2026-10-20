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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.testing

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent

/**
 * Telemetry test double that records what it is given, for any of the narrower contracts too:
 * pass it where a `DiagnosticsReporter`, an `AnalyticsReporter` or a `TelemetryCollection` is
 * expected.
 */
class FakeTelemetryRepository : TelemetryRepository {
    val loggedEvents: MutableList<AnalyticsEvent> = mutableListOf()
    val loggedScreenViews: MutableList<Pair<String, String?>> = mutableListOf()
    val breadcrumbs: MutableList<Pair<String, Map<String, String>>> = mutableListOf()
    val nonFatals: MutableList<Throwable> = mutableListOf()
    val viewModelErrors: MutableList<Throwable> = mutableListOf()

    /**
     * The last consent given to [updateConsent]: analytics, ad storage, ad user data and ad
     * personalization, in that order.
     */
    var consent: List<Boolean>? = null
        private set
    var analyticsEnabled: Boolean? = null
        private set
    var crashlyticsEnabled: Boolean? = null
        private set
    var performanceEnabled: Boolean? = null
        private set

    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) {
        consent = listOf(analyticsGranted, adStorageGranted, adUserDataGranted, adPersonalizationGranted)
    }

    override fun setAnalyticsEnabled(enabled: Boolean) {
        analyticsEnabled = enabled
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        crashlyticsEnabled = enabled
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        performanceEnabled = enabled
    }

    override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
        breadcrumbs.add(message to attributes)
    }

    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) {
        viewModelErrors.add(throwable)
    }

    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        nonFatals.add(throwable)
    }

    override fun logEvent(event: AnalyticsEvent) {
        loggedEvents.add(event)
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        loggedScreenViews.add(screenName to screenClass)
    }

    override fun setUserProperty(name: String, value: String?) {
    }
}
