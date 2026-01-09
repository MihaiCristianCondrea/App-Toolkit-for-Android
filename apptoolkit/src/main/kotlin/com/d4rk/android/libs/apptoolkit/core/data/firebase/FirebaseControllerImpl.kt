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

    private fun Boolean.toStatus(): FirebaseAnalytics.ConsentStatus =
        if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED
}
