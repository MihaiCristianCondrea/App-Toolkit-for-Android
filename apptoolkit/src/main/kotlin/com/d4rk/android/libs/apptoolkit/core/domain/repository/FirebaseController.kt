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

package com.d4rk.android.libs.apptoolkit.core.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent

/**
 * Controls runtime behaviors for Firebase services.
 *
 * Implementations should be the only place where the Firebase SDK is touched so
 * the rest of the codebase can toggle analytics, Crashlytics, and related
 * features through dependency injection.
 */
interface FirebaseController {

    /**
     * Updates the consent configuration used by Firebase Analytics.
     */
    fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    )

    /**
     * Enables or disables analytics collection for the current process.
     */
    fun setAnalyticsEnabled(enabled: Boolean)

    /**
     * Enables or disables Crashlytics crash reporting for the current process.
     */
    fun setCrashlyticsEnabled(enabled: Boolean)

    /**
     * Enables or disables Firebase Performance monitoring for the current process.
     */
    fun setPerformanceEnabled(enabled: Boolean)

    /**
     * Adds a breadcrumb to Crashlytics logs for tracing app events.
     *
     * @param message a short label describing the event
     * @param attributes optional key/value pairs to include with the breadcrumb
     */
    fun logBreadcrumb(
        message: String,
        attributes: Map<String, String> = emptyMap(),
    )

    /**
     * Reports a ViewModel flow failure to Firebase Crashlytics with useful context.
     *
     * Implementations should attach the ViewModel name, action identifier, and
     * throwable details as custom keys to aid debugging.
     *
     * @param viewModelName the ViewModel where the failure occurred
     * @param action label describing the flow or action that failed
     * @param throwable the exception that was caught
     * @param extraKeys optional extra context to attach as Crashlytics keys
     */
    fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String> = emptyMap(),
    )

    fun logEvent(event: AnalyticsEvent)

    fun logScreenView(screenName: String, screenClass: String? = null)

    fun setUserProperty(name: String, value: String?)
}
