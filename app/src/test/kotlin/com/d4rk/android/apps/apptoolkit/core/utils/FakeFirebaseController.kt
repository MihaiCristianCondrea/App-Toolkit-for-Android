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

package com.d4rk.android.apps.apptoolkit.core.utils

import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * No-op Firebase controller for unit tests.
 */
class FakeFirebaseController : FirebaseController {
    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) {
        // no-op
    }

    override fun setAnalyticsEnabled(enabled: Boolean) {
        // no-op
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        // no-op
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        // no-op
    }

    override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
        // no-op
    }

    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) {
        // no-op
    }

    override fun logEvent(event: AnalyticsEvent) {
        // no-op
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        // no-op
    }

    override fun setUserProperty(name: String, value: String?) {
        // no-op
    }
}
