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

package com.d4rk.android.libs.apptoolkit.core.data.remote.firebase

import android.os.Bundle
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.asConsentStatus
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance

/**
 * Firebase-backed implementation that delegates toggles to the SDK.
 */
class FirebaseControllerImpl : FirebaseController {

    private val analytics: FirebaseAnalytics
        get() = Firebase.analytics

    /**
     * Updates the Firebase Analytics consent settings based on the provided permissions.
     *
     * This method maps boolean flags to [FirebaseAnalytics.ConsentStatus] and applies them
     * to the Firebase SDK. This is essential for compliance with privacy regulations
     * like GDPR and CCPA.
     *
     * @param analyticsGranted Whether to allow storage related to analytics (e.g., cookies).
     * @param adStorageGranted Whether to allow storage related to advertising.
     * @param adUserDataGranted Whether to allow sending user data to Google for advertising purposes.
     * @param adPersonalizationGranted Whether to allow personalized advertising (retargeting).
     */
    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) {
        val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
        val consentSettings: MutableMap<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> =
            mutableMapOf()

        consentSettings[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE] =
            analyticsGranted.asConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_STORAGE] =
            adStorageGranted.asConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_USER_DATA] =
            adUserDataGranted.asConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION] =
            adPersonalizationGranted.asConsentStatus()

        firebaseAnalytics.setConsent(consentSettings)
    }

    /**
     * Enables or disables Firebase Analytics data collection.
     *
     * @param enabled Whether analytics collection should be enabled or disabled.
     */
    override fun setAnalyticsEnabled(enabled: Boolean) {
        Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
    }

    /**
     * Enables or disables Firebase Crashlytics data collection.
     *
     * When disabled, no crash reports or logs will be sent to Firebase.
     *
     * @param enabled True to enable Crashlytics collection, false to disable it.
     */
    override fun setCrashlyticsEnabled(enabled: Boolean) {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = enabled
    }

    /**
     * Enables or disables Firebase Performance Monitoring data collection.
     *
     * @param enabled True to enable performance collection, false to disable it.
     */
    override fun setPerformanceEnabled(enabled: Boolean) {
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = enabled
    }

    /**
     * Logs a breadcrumb message to Firebase Crashlytics to provide context for potential crashes.
     *
     * This method formats the provided message and attributes into a single string
     * and sends it to the Crashlytics log. Breadcrumbs help developers understand
     * the sequence of events leading up to an error.
     *
     * @param message The primary descriptive message for the breadcrumb.
     * @param attributes A map of key-value pairs providing additional context to the log.
     */
    override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val suffix = if (attributes.isEmpty()) {
            ""
        } else {
            attributes.entries.joinToString(prefix = " | ") { (key, value) ->
                "$key=$value"
            }
        }
        crashlytics.log("$message$suffix")
    }

    /**
     * Reports an error occurring within a ViewModel to Firebase Crashlytics.
     *
     * This method logs the exception along with specific metadata including the ViewModel's name,
     * the action being performed, and additional custom keys to facilitate debugging.
     *
     * @param viewModelName The name of the ViewModel where the error originated.
     * @param action A description of the operation or user action that triggered the error.
     * @param throwable The exception or error to be recorded.
     * @param extraKeys Additional key-value pairs to be attached as custom metadata in Crashlytics.
     */
    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("view_model", viewModelName)
        crashlytics.setCustomKey("action", action)
        crashlytics.setCustomKey("exception_type", throwable::class.java.name)
        crashlytics.setCustomKey("exception_message", throwable.message ?: "unknown")
        extraKeys.forEach { (key, value) ->
            crashlytics.setCustomKey(key, value)
        }
        crashlytics.log("ViewModel catch in $viewModelName during $action")
        crashlytics.recordException(throwable)
    }

    override fun logEvent(event: AnalyticsEvent) {
        val name = event.name
        if (!isValidEventName(name)) {
            logBreadcrumb(
                message = "analytics_drop_invalid_event",
                attributes = mapOf("name" to name)
            )
            return
        }

        val bundle = Bundle()
        var count = 0

        for ((key, rawValue) in event.params) {
            if (count >= MAX_PARAMS) break
            if (!isValidParamName(key)) {
                logBreadcrumb(
                    message = "analytics_drop_invalid_param",
                    attributes = mapOf("event" to name, "param" to key)
                )
                continue
            }

            when (rawValue) {
                is AnalyticsValue.Str -> bundle.putString(
                    key,
                    rawValue.value.take(MAX_PARAM_STRING_LEN)
                )

                is AnalyticsValue.LongVal -> bundle.putLong(key, rawValue.value)
                is AnalyticsValue.DoubleVal -> bundle.putDouble(key, rawValue.value)
                is AnalyticsValue.Bool -> bundle.putString(
                    key,
                    rawValue.value.toString()
                ) // GA4 accepts boolean as string or int; keep consistent
            }
            count++
        }

        analytics.logEvent(name, bundle)
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        val safeName = screenName.take(MAX_PARAM_STRING_LEN)

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, safeName)
            if (!screenClass.isNullOrBlank()) {
                putString(
                    FirebaseAnalytics.Param.SCREEN_CLASS,
                    screenClass.take(MAX_PARAM_STRING_LEN)
                )
            }
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun setUserProperty(name: String, value: String?) {
        val trimmedName = name.take(MAX_USER_PROP_NAME_LEN)
        if (!isValidUserPropertyName(trimmedName)) {
            logBreadcrumb(
                message = "analytics_drop_invalid_user_property",
                attributes = mapOf("name" to name)
            )
            return
        }
        val trimmedValue = value?.take(MAX_USER_PROP_VALUE_LEN)
        analytics.setUserProperty(trimmedName, trimmedValue)
    }

    private fun isValidEventName(name: String): Boolean =
        NAME_REGEX.matches(name) && RESERVED_EVENT_PREFIXES.none(name::startsWith)

    private fun isValidParamName(name: String): Boolean =
        NAME_REGEX.matches(name) && RESERVED_PARAM_PREFIXES.none(name::startsWith)

    private fun isValidUserPropertyName(name: String): Boolean =
        USER_PROPERTY_NAME_REGEX.matches(name) && RESERVED_USER_PROPERTY_PREFIXES.none(name::startsWith)

    private companion object {
        const val MAX_PARAMS = 25
        const val MAX_PARAM_STRING_LEN = 100
        const val MAX_USER_PROP_NAME_LEN = 24
        const val MAX_USER_PROP_VALUE_LEN = 36

        val NAME_REGEX = Regex("^[A-Za-z][A-Za-z0-9_]{0,39}$")
        val USER_PROPERTY_NAME_REGEX = Regex("^[A-Za-z][A-Za-z0-9_]{0,23}$")

        val RESERVED_EVENT_PREFIXES = listOf("firebase_", "google_", "ga_")
        val RESERVED_PARAM_PREFIXES = listOf("firebase_", "google_", "ga_", "_")
        val RESERVED_USER_PROPERTY_PREFIXES = listOf("firebase_", "google_", "ga_")
    }
}
