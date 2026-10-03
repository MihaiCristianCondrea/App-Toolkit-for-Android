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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent

/**
 * A [TelemetryRepository] that drops everything it is given.
 *
 * For previews and tests that render code which reports, and for the default of `LocalTelemetry`
 * where nothing provides the real one.
 */
object NoOpTelemetryRepository : TelemetryRepository {
    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) = Unit

    override fun setAnalyticsEnabled(enabled: Boolean) = Unit

    override fun setCrashlyticsEnabled(enabled: Boolean) = Unit

    override fun setPerformanceEnabled(enabled: Boolean) = Unit

    override fun logBreadcrumb(message: String, attributes: Map<String, String>) = Unit

    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) = Unit

    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) = Unit

    override fun logEvent(event: AnalyticsEvent) = Unit

    override fun logScreenView(screenName: String, screenClass: String?) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit
}
