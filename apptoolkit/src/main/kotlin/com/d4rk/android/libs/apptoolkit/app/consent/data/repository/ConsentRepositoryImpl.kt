package com.d4rk.android.libs.apptoolkit.app.consent.data.repository

import com.d4rk.android.libs.apptoolkit.app.consent.data.local.ConsentPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Implementation of [ConsentRepository] that delegates UMP work to a remote data source.
 */
class ConsentRepositoryImpl(
    private val remote: ConsentRemoteDataSource,
    private val local: ConsentPreferencesDataSource,
    private val configProvider: BuildInfoProvider,
    private val firebaseController: FirebaseController,
) : ConsentRepository {

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        return remote.requestConsent(host = host, showIfRequired = showIfRequired)
    }

    override suspend fun applyInitialConsent() {
        val settings = readPersistedSettings()
        applyConsentSettings(settings)
    }

    override suspend fun applyConsentSettings(settings: ConsentSettings) {
        firebaseController.updateConsent(
            analyticsGranted = settings.analyticsConsent,
            adStorageGranted = settings.adStorageConsent,
            adUserDataGranted = settings.adUserDataConsent,
            adPersonalizationGranted = settings.adPersonalizationConsent,
        )
        firebaseController.setAnalyticsEnabled(settings.usageAndDiagnostics)
        firebaseController.setCrashlyticsEnabled(settings.usageAndDiagnostics)
        firebaseController.setPerformanceEnabled(settings.usageAndDiagnostics)
    }

    private suspend fun readPersistedSettings(): ConsentSettings = coroutineScope {
        val defaultGranted = !configProvider.isDebugBuild
        val usageDeferred = async {
            local.usageAndDiagnostics(default = defaultGranted).first()
        }
        val analyticsDeferred = async {
            local.analyticsConsent(default = defaultGranted).first()
        }
        val adStorageDeferred = async {
            local.adStorageConsent(default = defaultGranted).first()
        }
        val adUserDataDeferred = async {
            local.adUserDataConsent(default = defaultGranted).first()
        }
        val adPersonalizationDeferred = async {
            local.adPersonalizationConsent(default = defaultGranted).first()
        }

        ConsentSettings(
            usageAndDiagnostics = usageDeferred.await(),
            analyticsConsent = analyticsDeferred.await(),
            adStorageConsent = adStorageDeferred.await(),
            adUserDataConsent = adUserDataDeferred.await(),
            adPersonalizationConsent = adPersonalizationDeferred.await(),
        )
    }
}
