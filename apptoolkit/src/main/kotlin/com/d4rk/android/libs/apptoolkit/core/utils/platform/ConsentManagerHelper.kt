package com.d4rk.android.libs.apptoolkit.core.utils.platform

import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
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
    private val firebaseController: FirebaseController by inject()
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
        firebaseController.updateConsent(
            analyticsGranted = analyticsGranted,
            adStorageGranted = adStorageGranted,
            adUserDataGranted = adUserDataGranted,
            adPersonalizationGranted = adPersonalizationGranted,
        )
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
        firebaseController.setAnalyticsEnabled(usageAndDiagnosticsGranted)
        firebaseController.setCrashlyticsEnabled(usageAndDiagnosticsGranted)
        firebaseController.setPerformanceEnabled(usageAndDiagnosticsGranted)
    }
}
