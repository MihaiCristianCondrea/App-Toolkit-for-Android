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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.isAlive
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

/**
 * Implementation of [ConsentRepository] that delegates UMP work to a remote data source.
 *
 * @param requestScope scope that owns the shared, process-wide consent round trip. It defaults to
 * the immediate main dispatcher because UMP requires its entry points to be called from the main
 * thread.
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
     * Requests consent, sharing one UMP round trip between callers that ask at the same time, since
     * overlapping requests drive the SDK into the failure path that crashes the process from its
     * own executor. A host that is finishing or destroyed is rejected before UMP is called.
     *
     * The flight is keyed on [showIfRequired], so an explicit request for the form never takes the
     * answer of one that shows it only when required. A caller joins only a request whose host is
     * still alive; a request from a host that has gone is waited for, at most
     * [STALE_REQUEST_WAIT_MS], and then a new one starts.
     */
    override suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ) {
        val hostName: String = host.activity::class.java.name
        telemetryRepository.logBreadcrumb(
            message = "Consent request started",
            attributes = mapOf(
                "host" to hostName,
                "showIfRequired" to showIfRequired.toString(),
            ),
        )
        if (!host.isAlive) {
            telemetryRepository.logBreadcrumb(
                message = "Consent request skipped for a finishing host",
                attributes = mapOf("host" to hostName),
            )
            throw ConsentException(
                reason = ConsentException.Reason.HOST_UNAVAILABLE,
                message = "Consent host is finishing or destroyed.",
            )
        }

        val staleRequest: InFlightConsentRequest? = requestMutex.withLock {
            inFlightRequest?.takeIf { !it.host.isAlive }
        }
        if (staleRequest != null) {
            telemetryRepository.logBreadcrumb(
                message = "Consent request waiting for a request from a finished host",
                attributes = mapOf("host" to hostName),
            )
            withTimeoutOrNull(STALE_REQUEST_WAIT_MS.milliseconds) { staleRequest.result.join() }
        }

        val request: InFlightConsentRequest = requestMutex.withLock {
            inFlightRequest
                ?.takeIf { it.showIfRequired == showIfRequired && it.host.isAlive }
                ?.also {
                    telemetryRepository.logBreadcrumb(
                        message = "Consent request joined an in-flight request",
                        attributes = mapOf("host" to hostName),
                    )
                }
                ?: startRequest(host = host, showIfRequired = showIfRequired)
        }

        request.result.await()
    }

    /**
     * Starts a shared UMP round trip in [requestScope] and publishes its outcome through
     * [InFlightConsentRequest.result], so a caller that stops waiting never cancels it for the
     * others. A round trip cancelled with its scope ends as [ConsentException.Reason.REQUEST_FAILED].
     *
     * The caller must hold [requestMutex].
     */
    private fun startRequest(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): InFlightConsentRequest {
        val request = InFlightConsentRequest(
            host = host,
            showIfRequired = showIfRequired,
            result = CompletableDeferred(),
        )
        inFlightRequest = request

        requestScope.launch {
            try {
                remote.requestConsent(host = host, showIfRequired = showIfRequired)
                request.result.complete(Unit)
            } catch (cancellation: CancellationException) {
                request.result.completeExceptionally(
                    ConsentException(
                        reason = ConsentException.Reason.REQUEST_FAILED,
                        message = "Consent round trip was cancelled before UMP answered.",
                        cause = cancellation,
                    )
                )
                throw cancellation
            } catch (throwable: Throwable) {
                request.result.completeExceptionally(throwable)
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
        val result: CompletableDeferred<Unit>,
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

    /** Reads every stored choice at once. Unset choices are granted in release builds only. */
    private suspend fun readPersistedSettings(): ConsentSettings = storageCall {
        coroutineScope {
            val defaultGranted = !configProvider.isDebugBuild
            val usageDeferred = async { local.usageAndDiagnostics(default = defaultGranted).first() }
            val analyticsDeferred = async { local.analyticsConsent(default = defaultGranted).first() }
            val adStorageDeferred = async { local.adStorageConsent(default = defaultGranted).first() }
            val adUserDataDeferred = async { local.adUserDataConsent(default = defaultGranted).first() }
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
}

/**
 * How long a new host waits for a consent request started by a host that has since gone. UMP
 * answers a request whose host can no longer show a form within moments, so this only matters
 * when it never answers.
 */
private const val STALE_REQUEST_WAIT_MS: Long = 5_000L
