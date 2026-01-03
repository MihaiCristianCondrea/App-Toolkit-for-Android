package com.d4rk.android.libs.apptoolkit.core.data.firebase

import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance

/**
 * Firebase-backed implementation that delegates toggles to the SDK.
 */
class FirebaseControllerImpl : FirebaseController {

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
            analyticsGranted.toStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_STORAGE] =
            adStorageGranted.toStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_USER_DATA] =
            adUserDataGranted.toStatus()
        consentSettings[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION] =
            adPersonalizationGranted.toStatus()

        firebaseAnalytics.setConsent(consentSettings)
    }

    override fun setAnalyticsEnabled(enabled: Boolean) {
        Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = enabled
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = enabled
    }

    private fun Boolean.toStatus(): FirebaseAnalytics.ConsentStatus =
        if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED
}
