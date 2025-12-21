package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Helper responsible for applying user consent preferences to Firebase services.
 *
 * It reads persisted flags from [CommonDataStore] and propagates them to
 * Analytics, Crashlytics and Performance so data collection respects the
 * user's choices.
 */
object ConsentManagerHelper : KoinComponent {

    private val configProvider: BuildInfoProvider by inject()
    val defaultAnalyticsGranted: Boolean by lazy { !configProvider.isDebugBuild }

    /**
     * Updates the user's consent settings in Firebase Analytics.
     *
     * This function assumes the "Usage and Diagnostics" toggle controls all four consent types
     * (ANALYTICS_STORAGE, AD_STORAGE, AD_USER_DATA, AD_PERSONALIZATION).
     * If your "Usage and Diagnostics" toggle has a more limited scope (e.g., only analytics),
     * you MUST adjust the logic below to correctly set only the relevant consent types
     * and decide how the other types are managed (e.g., separate toggles, manifest defaults).
     *
     */
    fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean
    ) {
        val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
        val consentSettings: MutableMap<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> =
            mutableMapOf()

        consentSettings[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE] =
            if (analyticsGranted) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED

        consentSettings[FirebaseAnalytics.ConsentType.AD_STORAGE] =
            if (adStorageGranted) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED

        consentSettings[FirebaseAnalytics.ConsentType.AD_USER_DATA] =
            if (adUserDataGranted) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED

        consentSettings[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION] =
            if (adPersonalizationGranted) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED

        firebaseAnalytics.setConsent(consentSettings)
    }


    /**
     * Reads the persisted "Usage and Diagnostics" setting from DataStore and applies
     * it to Firebase Analytics consent settings on app startup.
     *
     * @param dataStore Your instance of CommonDataStore.
     */
    suspend fun applyInitialConsent(dataStore: CommonDataStore) {
        val (
            analyticsGranted,
            adStorageGranted,
            adUserDataGranted,
            adPersonalizationGranted
        ) = coroutineScope {
            val analyticsDeferred = async {
                dataStore.analyticsConsent(default = defaultAnalyticsGranted).first()
            }
            val adStorageDeferred = async {
                dataStore.adStorageConsent(default = defaultAnalyticsGranted).first()
            }
            val adUserDataDeferred = async {
                dataStore.adUserDataConsent(default = defaultAnalyticsGranted).first()
            }
            val adPersonalizationDeferred = async {
                dataStore.adPersonalizationConsent(default = defaultAnalyticsGranted).first()
            }

            awaitAll(
                analyticsDeferred,
                adStorageDeferred,
                adUserDataDeferred,
                adPersonalizationDeferred
            )
        }

        updateConsent(
            analyticsGranted = analyticsGranted,
            adStorageGranted = adStorageGranted,
            adUserDataGranted = adUserDataGranted,
            adPersonalizationGranted = adPersonalizationGranted
        )

        updateAnalyticsCollectionFromDatastore(dataStore = dataStore)
    }

    /**
     * Applies the persisted "Usage and Diagnostics" preference to Firebase SDKs.
     *
     * @param dataStore source of the user's consent setting
     */
    suspend fun updateAnalyticsCollectionFromDatastore(dataStore: CommonDataStore) {
        val usageAndDiagnosticsGranted: Boolean =
            dataStore.usageAndDiagnostics(default = defaultAnalyticsGranted).first()
        Firebase.analytics.setAnalyticsCollectionEnabled(usageAndDiagnosticsGranted)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled =
            usageAndDiagnosticsGranted
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled =
            usageAndDiagnosticsGranted
    }
}