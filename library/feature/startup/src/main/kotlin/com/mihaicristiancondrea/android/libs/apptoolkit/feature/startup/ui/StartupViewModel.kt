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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states.ConsentRequestStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states.StartupUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import kotlinx.coroutines.Job
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the startup screen: the consent request that has to settle before the person can
 * continue.
 *
 * [ConsentRepository] is main-safe, so it needs no dispatcher. Consent settles whatever the
 * outcome, so the screen never waits on a form that failed.
 */
class StartupViewModel(
    private val consentRepository: ConsentRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<StartupUiState, StartupEvent>(
    initialState = StartupUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Startup",
    viewModelName = "StartupViewModel",
) {
    private var consentJob: Job? = null

    override fun handleEvent(event: StartupEvent) {
        when (event) {
            is StartupEvent.RequestConsent -> requestConsent(host = event.host)
        }
    }

    /**
     * Waits at most [CONSENT_TIMEOUT] for consent. This is the app's first screen and its only way
     * forward is the button shown once consent settles, so a round trip that never reports back
     * must not keep the person here. A later resume restarts a request that is still waiting.
     */
    private fun requestConsent(host: ConsentHost?) {
        if (currentState.consent == ConsentRequestStatus.Settled) return
        consentJob = consentJob.restart {
            launchReport(
                action = Actions.REQUEST_CONSENT,
                onError = { settleConsent() },
            ) {
                if (host != null) {
                    withTimeoutOrNull(CONSENT_TIMEOUT) {
                        consentRepository.requestConsent(host = host)
                    }
                }
                settleConsent()
            }
        }
    }

    private fun settleConsent() {
        setState { copy(consent = ConsentRequestStatus.Settled) }
    }

    private object Actions {
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private companion object {
        /** How long the startup screen waits for consent before letting the person carry on. */
        val CONSENT_TIMEOUT: Duration = 15.seconds
    }
}
