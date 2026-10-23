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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryCollection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.isAlive
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.ConsentRemoteDataSource
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Implementation of [ConsentRepository] that delegates UMP work to a remote data source.
 *
 * @param diagnostics breadcrumbs that trace the consent lifecycle; the repository reports no
 * analytics events.
 * @param collection applies the person's consent to the telemetry SDKs' collection switches.
 * @param requestScope scope that owns the shared, process-wide consent round trip. It defaults to
 * the immediate main dispatcher because UMP requires its entry points to be called from the main
 * thread.
 */
class DefaultConsentRepository(
    private val remote: ConsentRemoteDataSource,
    private val local: ConsentPreferencesDataSource,
    private val buildInfoRepository: BuildInfoRepository,
    private val diagnostics: DiagnosticsReporter,
    private val collection: TelemetryCollection,
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
     * still alive. Incompatible requests wait for the active SDK operation before starting.
     * A stale host is waited for at most [STALE_REQUEST_WAIT_MS]; a timeout fails the new caller
     * and leaves the active operation tracked, so it cannot overlap another SDK request.
     */
    override suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ) {
        val hostName: String = host.activity::class.java.name
        diagnostics.logBreadcrumb(
            message = "Consent request started",
            attributes = mapOf(
                "host" to hostName,
                "showIfRequired" to showIfRequired.toString(),
            ),
        )
        if (!host.isAlive) {
            diagnostics.logBreadcrumb(
                message = "Consent request skipped for a finishing host",
                attributes = mapOf("host" to hostName),
            )
            throw ConsentException(
                reason = ConsentException.Reason.HOST_UNAVAILABLE,
                message = "Consent host is finishing or destroyed.",
            )
        }

        while (true) {
            if (!host.isAlive) {
                throw ConsentException(
                    reason = ConsentException.Reason.HOST_UNAVAILABLE,
                    message = "Consent host is finishing or destroyed.",
                )
            }
            val (request, canJoin) = requestMutex.withLock {
                val current = inFlightRequest?.takeUnless { it.result.isCompleted }
                if (current == null) {
                    startRequest(host = host, showIfRequired = showIfRequired) to true
                } else {
                    current to (current.showIfRequired == showIfRequired && current.host.isAlive)
                }
            }
            if (canJoin) {
                request.result.await()
                return
            }

            // A distinct request needs its own result, but must wait for the active SDK operation.
            if (request.host.isAlive) {
                request.result.join()
            } else {
                val finished = withTimeoutOrNull(STALE_REQUEST_WAIT_MS.milliseconds) {
                    request.result.join()
                    true
                } ?: false
                if (!finished) {
                    throw ConsentException(
                        reason = ConsentException.Reason.REQUEST_FAILED,
                        message = "The previous consent request has not completed.",
                    )
                }
            }
        }
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
        diagnostics.logBreadcrumb(message = "Applying initial consent")
        val settings = readPersistedSettings()
        applyConsentSettings(settings)
    }

    override suspend fun applyConsentSettings(settings: ConsentSettings) {
        diagnostics.logBreadcrumb(
            message = "Consent settings applied",
            attributes = mapOf(
                "usageAndDiagnostics" to settings.usageAndDiagnostics.toString(),
                "analyticsConsent" to settings.analyticsConsent.toString(),
                "adStorageConsent" to settings.adStorageConsent.toString(),
                "adUserDataConsent" to settings.adUserDataConsent.toString(),
                "adPersonalizationConsent" to settings.adPersonalizationConsent.toString(),
            ),
        )
        collection.updateConsent(
            analyticsGranted = settings.analyticsConsent,
            adStorageGranted = settings.adStorageConsent,
            adUserDataGranted = settings.adUserDataConsent,
            adPersonalizationGranted = settings.adPersonalizationConsent,
        )
        collection.setAnalyticsEnabled(settings.usageAndDiagnostics)
        collection.setCrashlyticsEnabled(settings.usageAndDiagnostics)
        collection.setPerformanceEnabled(settings.usageAndDiagnostics)
    }

    /** Reads every choice from one Preferences emission. Unset choices are granted in release only. */
    private suspend fun readPersistedSettings(): ConsentSettings = storageCall {
        local.settings(default = !buildInfoRepository.isDebugBuild).first()
    }

}

/**
 * How long a new host waits for a consent request started by a host that has since gone. UMP
 * answers a request whose host can no longer show a form within moments, so this only matters
 * when it never answers.
 */
private const val STALE_REQUEST_WAIT_MS: Long = 5_000L
