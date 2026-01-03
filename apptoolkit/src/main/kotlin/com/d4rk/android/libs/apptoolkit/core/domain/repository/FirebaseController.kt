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
}
