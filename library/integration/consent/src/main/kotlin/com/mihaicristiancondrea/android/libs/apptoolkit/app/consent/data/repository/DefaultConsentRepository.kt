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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.isAlive
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repository.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Implementation of [ConsentRepository] that delegates UMP work to a remote data source.
 *
 * @param requestScope scope that owns the shared, process-wide consent round trip. It deliberately
 * defaults to the immediate main dispatcher because UMP requires its entry points to be called from
 * the main thread.
 */
class DefaultConsentRepository(
    private val remote: ConsentRemoteDataSource,
    private val local: ConsentPreferencesDataSource,
    private val configProvider: BuildInfoProvider,
    private val firebaseController: FirebaseController,
    private val requestScope: CoroutineScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Main.immediate),
) : ConsentRepository {

    private val requestMutex = Mutex()
    private var inFlightRequest: InFlightConsentRequest? = null

    /**
     * Requests consent, sharing a single UMP round trip between concurrent callers.
     *
     * Change rationale: consent used to be requested straight from the data source, once per
     * caller. `OnboardingActivity`, `StartupActivity`, and each host's `MainActivity` can all ask
     * within the same second, which produced overlapping UMP requests — including requests issued
     * by an activity that was already finishing. Overlapping requests are what drives the SDK into
     * its failure path, and a failing metrics ping with an empty error body crashes the process
     * from the SDK's own executor where no caller-side `catch` can reach it.
     *
     * A second caller now attaches to the request that is already running instead of starting
     * another one; requests from a host that is finishing or destroyed are rejected outright. The
     * flight is keyed on [showIfRequired] so an explicit "show the form now" request is never
     * silently answered by an in-flight "show only if required" one.
     */
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flow {
        firebaseController.logBreadcrumb(
            message = "Consent request started",
            attributes = mapOf(
                "host" to host.activity::class.java.name,
                "showIfRequired" to showIfRequired.toString(),
            ),
        )
        if (!host.isAlive) {
            firebaseController.logBreadcrumb(
                message = "Consent request skipped for a finishing host",
                attributes = mapOf("host" to host.activity::class.java.name),
            )
            emit(DataState.Loading())
            emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
            return@flow
        }

        val request: InFlightConsentRequest = requestMutex.withLock {
            inFlightRequest
                ?.takeIf { it.showIfRequired == showIfRequired }
                ?.also {
                    firebaseController.logBreadcrumb(
                        message = "Consent request joined an in-flight request",
                        attributes = mapOf("host" to host.activity::class.java.name),
                    )
                }
                ?: startRequest(host = host, showIfRequired = showIfRequired)
        }

        emit(DataState.Loading())
        emit(request.state.first { dataState -> dataState !is DataState.Loading })
    }

    /**
     * Starts a shared UMP round trip and publishes its states through a replaying [StateFlow].
     *
     * The caller must hold [requestMutex].
     */
    private fun startRequest(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): InFlightConsentRequest {
        val state = MutableStateFlow<DataState<Unit, Errors.UseCase>>(value = DataState.Loading())
        val request = InFlightConsentRequest(showIfRequired = showIfRequired, state = state)
        inFlightRequest = request

        requestScope.launch {
            try {
                remote.requestConsent(host = host, showIfRequired = showIfRequired)
                    .collect { dataState -> state.value = dataState }
                if (state.value is DataState.Loading) {
                    state.value = DataState.Error(
                        error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO,
                    )
                }
            } finally {
                withContext(NonCancellable) {
                    requestMutex.withLock {
                        if (inFlightRequest === request) {
                            inFlightRequest = null
                        }
                    }
                }
            }
        }

        return request
    }

    /** A consent round trip that later callers can attach to. */
    private class InFlightConsentRequest(
        val showIfRequired: Boolean,
        val state: MutableStateFlow<DataState<Unit, Errors.UseCase>>,
    )

    override suspend fun applyInitialConsent() {
        firebaseController.logBreadcrumb(message = "Applying initial consent")
        val settings = readPersistedSettings()
        applyConsentSettings(settings)
    }

    override suspend fun applyConsentSettings(settings: ConsentSettings) {
        firebaseController.logBreadcrumb(
            message = "Consent settings applied",
            attributes = mapOf(
                "usageAndDiagnostics" to settings.usageAndDiagnostics.toString(),
                "analyticsConsent" to settings.analyticsConsent.toString(),
                "adStorageConsent" to settings.adStorageConsent.toString(),
                "adUserDataConsent" to settings.adUserDataConsent.toString(),
                "adPersonalizationConsent" to settings.adPersonalizationConsent.toString(),
            ),
        )
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


