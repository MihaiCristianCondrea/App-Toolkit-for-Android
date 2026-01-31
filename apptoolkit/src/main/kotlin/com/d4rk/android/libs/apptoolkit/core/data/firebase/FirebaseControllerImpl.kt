package com.d4rk.android.libs.apptoolkit.core.data.firebase

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
}
