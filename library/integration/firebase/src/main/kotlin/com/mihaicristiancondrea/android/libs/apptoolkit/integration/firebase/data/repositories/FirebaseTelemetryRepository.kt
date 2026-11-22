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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.repositories

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.mappers.toFirebaseConsentStatus

/**
 * [TelemetryRepository] backed by Firebase Analytics, Crashlytics and Performance Monitoring.
 *
 * If Firebase is not initialized in the current process, SDK lookups fail closed and no-op so
 * telemetry calls never crash callers or mask an exception being reported.
 */
class FirebaseTelemetryRepository(
    private val analyticsProvider: () -> FirebaseAnalytics = { Firebase.analytics },
    private val crashlyticsProvider: () -> FirebaseCrashlytics = { FirebaseCrashlytics.getInstance() },
    private val performanceProvider: () -> FirebasePerformance = { FirebasePerformance.getInstance() },
) : TelemetryRepository {

    private fun analyticsOrNull(): FirebaseAnalytics? =
        runCatching { analyticsProvider() }.getOrNull()

    private fun crashlyticsOrNull(): FirebaseCrashlytics? =
        runCatching { crashlyticsProvider() }.getOrNull()

    private fun performanceOrNull(): FirebasePerformance? =
        runCatching { performanceProvider() }.getOrNull()

    /**
     * Applies the four stored consent choices to their corresponding Firebase Analytics consent
     * types.
     */
    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) {
        val firebaseAnalytics: FirebaseAnalytics = analyticsOrNull() ?: return
        val consentSettings: MutableMap<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> =
            mutableMapOf()

        consentSettings[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE] =
            analyticsGranted.toFirebaseConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_STORAGE] =
            adStorageGranted.toFirebaseConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_USER_DATA] =
            adUserDataGranted.toFirebaseConsentStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION] =
            adPersonalizationGranted.toFirebaseConsentStatus()

        firebaseAnalytics.setConsent(consentSettings)
    }

    override fun setAnalyticsEnabled(enabled: Boolean) {
        analyticsOrNull()?.setAnalyticsCollectionEnabled(enabled)
    }

    /**
     * Controls automatic Crashlytics collection through the SDK collection flag.
     */
    override fun setCrashlyticsEnabled(enabled: Boolean) {
        crashlyticsOrNull()?.isCrashlyticsCollectionEnabled = enabled
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        performanceOrNull()?.isPerformanceCollectionEnabled = enabled
    }

    /**
     * Appends attributes to the message and records it in the Crashlytics breadcrumb log.
     */
    override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
        val crashlytics = crashlyticsOrNull() ?: return
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
     * Records a ViewModel failure with operation and caller-supplied metadata scoped to this
     * exception, without setting persistent Crashlytics keys.
     */
    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) {
        val crashlytics = crashlyticsOrNull() ?: return
        crashlytics.log("ViewModel catch in $viewModelName during $action")
        crashlytics.recordException(
            throwable,
            reportKeys(
                throwable = throwable,
                keys = extraKeys + mapOf("view_model" to viewModelName, "action" to action),
            ),
        )
    }

    /**
     * Records a non-fatal failure with attributes scoped to this exception.
     */
    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        val crashlytics = crashlyticsOrNull() ?: return
        crashlytics.recordException(
            throwable,
            reportKeys(throwable = throwable, keys = attributes),
        )
    }

    /**
     * Keeps metadata local to one exception; persistent custom keys would leak into unrelated
     * later reports.
     */
    private fun reportKeys(throwable: Throwable, keys: Map<String, String>): CustomKeysAndValues =
        CustomKeysAndValues.Builder()
            .putString("exception_type", throwable::class.java.name)
            .putString("exception_message", throwable.message ?: "unknown")
            .apply { keys.forEach { (key, value) -> putString(key, value) } }
            .build()

    override fun logEvent(event: AnalyticsEvent) {
        val name = event.name
        if (!isValidEventName(name)) {
            logBreadcrumb(
                message = "analytics_drop_invalid_event",
                attributes = mapOf("name" to name),
            )
            return
        }
        val analytics: FirebaseAnalytics = analyticsOrNull() ?: return

        val bundle = Bundle()
        var count = 0

        for ((key, rawValue) in event.params) {
            if (count >= MAX_PARAMS) break
            if (!isValidParamName(key)) {
                logBreadcrumb(
                    message = "analytics_drop_invalid_param",
                    attributes = mapOf("event" to name, "param" to key),
                )
                continue
            }

            when (rawValue) {
                is AnalyticsValue.Str -> bundle.putString(
                    key,
                    rawValue.value.take(MAX_PARAM_STRING_LEN),
                )

                is AnalyticsValue.LongVal -> bundle.putLong(key, rawValue.value)
                is AnalyticsValue.DoubleVal -> {
                    if (!rawValue.value.isFinite()) {
                        logBreadcrumb(
                            message = "analytics_drop_non_finite_param",
                            attributes = mapOf("event" to name, "param" to key),
                        )
                        continue
                    }
                    bundle.putDouble(key, rawValue.value)
                }

                is AnalyticsValue.Bool -> bundle.putString(
                    key,
                    rawValue.value.toString(),
                ) // Encode booleans consistently as strings.
            }
            count++
        }

        analytics.logEvent(name, bundle)
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        val safeName = screenName.trim().take(MAX_PARAM_STRING_LEN)
        if (safeName.isEmpty()) {
            logBreadcrumb(message = "analytics_drop_invalid_screen_view")
            return
        }
        val analytics: FirebaseAnalytics = analyticsOrNull() ?: return

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, safeName)
            if (!screenClass.isNullOrBlank()) {
                putString(
                    FirebaseAnalytics.Param.SCREEN_CLASS,
                    screenClass.take(MAX_PARAM_STRING_LEN),
                )
            }
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun setUserProperty(name: String, value: String?) {
        if (!isValidUserPropertyName(name)) {
            logBreadcrumb(
                message = "analytics_drop_invalid_user_property",
                attributes = mapOf("name" to name),
            )
            return
        }
        val trimmedValue = value?.take(MAX_USER_PROP_VALUE_LEN)
        analyticsOrNull()?.setUserProperty(name, trimmedValue)
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
        const val MAX_USER_PROP_VALUE_LEN = 36

        val NAME_REGEX = Regex("^[A-Za-z][A-Za-z0-9_]{0,39}$")
        val USER_PROPERTY_NAME_REGEX = Regex("^[A-Za-z][A-Za-z0-9_]{0,23}$")

        val RESERVED_EVENT_PREFIXES = listOf("firebase_", "google_", "ga_")
        val RESERVED_PARAM_PREFIXES = listOf("firebase_", "google_", "ga_")
        val RESERVED_USER_PROPERTY_PREFIXES = listOf("firebase_", "google_", "ga_")
    }
}
