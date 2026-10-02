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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.isAlive
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

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
    private val telemetryRepository: TelemetryRepository,
    private val requestScope: CoroutineScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Main.immediate),
) : ConsentRepository {

    private val requestMutex = Mutex()
    private var inFlightRequest: InFlightConsentRequest? = null

    /**
     * Shares an in-flight UMP request with callers using the same [showIfRequired] mode and a
     * live host. Finishing or destroyed hosts are rejected.
     *
     * A replacement host waits up to [STALE_REQUEST_WAIT_MS] for a dead host's request to
     * settle before starting its own, limiting overlap without letting an unanswered request
     * block later callers indefinitely.
     */
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flow {
        telemetryRepository.logBreadcrumb(
            message = "Consent request started",
            attributes = mapOf(
                "host" to host.activity::class.java.name,
                "showIfRequired" to showIfRequired.toString(),
            ),
        )
        if (!host.isAlive) {
            telemetryRepository.logBreadcrumb(
                message = "Consent request skipped for a finishing host",
                attributes = mapOf("host" to host.activity::class.java.name),
            )
            emit(DataState.Loading())
            emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
            return@flow
        }

        val staleRequest: InFlightConsentRequest? = requestMutex.withLock {
            inFlightRequest?.takeIf { !it.host.isAlive }
        }
        if (staleRequest != null) {
            telemetryRepository.logBreadcrumb(
                message = "Consent request waiting for a request from a finished host",
                attributes = mapOf("host" to host.activity::class.java.name),
            )
            withTimeoutOrNull(STALE_REQUEST_WAIT_MS.milliseconds) {
                staleRequest.state.first { dataState -> dataState !is DataState.Loading }
            }
        }

        val request: InFlightConsentRequest = requestMutex.withLock {
            inFlightRequest
                ?.takeIf { it.showIfRequired == showIfRequired && it.host.isAlive }
                ?.also {
                    telemetryRepository.logBreadcrumb(
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
        val request = InFlightConsentRequest(
            host = host,
            showIfRequired = showIfRequired,
            state = state,
        )
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

    /** A consent round trip that later callers can attach to while its [host] is alive. */
    private class InFlightConsentRequest(
        val host: ConsentHost,
        val showIfRequired: Boolean,
        val state: MutableStateFlow<DataState<Unit, Errors.UseCase>>,
    )

    override suspend fun applyInitialConsent() {
        telemetryRepository.logBreadcrumb(message = "Applying initial consent")
        val settings = readPersistedSettings()
        applyConsentSettings(settings)
    }

    override suspend fun applyConsentSettings(settings: ConsentSettings) {
        telemetryRepository.logBreadcrumb(
            message = "Consent settings applied",
            attributes = mapOf(
                "usageAndDiagnostics" to settings.usageAndDiagnostics.toString(),
                "analyticsConsent" to settings.analyticsConsent.toString(),
                "adStorageConsent" to settings.adStorageConsent.toString(),
                "adUserDataConsent" to settings.adUserDataConsent.toString(),
                "adPersonalizationConsent" to settings.adPersonalizationConsent.toString(),
            ),
        )
        telemetryRepository.updateConsent(
            analyticsGranted = settings.analyticsConsent,
            adStorageGranted = settings.adStorageConsent,
            adUserDataGranted = settings.adUserDataConsent,
            adPersonalizationGranted = settings.adPersonalizationConsent,
        )
        telemetryRepository.setAnalyticsEnabled(settings.usageAndDiagnostics)
        telemetryRepository.setCrashlyticsEnabled(settings.usageAndDiagnostics)
        telemetryRepository.setPerformanceEnabled(settings.usageAndDiagnostics)
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

/**
 * Bounds waiting for an unanswered request whose host has disappeared.
 */
private const val STALE_REQUEST_WAIT_MS: Long = 5_000L


