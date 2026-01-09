package com.d4rk.android.libs.apptoolkit.core.domain.repository

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
}
