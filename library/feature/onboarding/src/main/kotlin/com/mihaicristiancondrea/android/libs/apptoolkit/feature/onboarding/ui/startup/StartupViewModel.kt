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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.contracts.StartupAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.states.StartupUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.successData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the startup screen.
 *
 * Consent is requested by the UI layer after receiving [StartupAction.RequestConsentUi], so this
 * ViewModel does not keep Activity-bound host references.
 */
class StartupViewModel(
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<StartupUiState, StartupEvent, StartupAction>(
    initialState = UiStateScreen(data = StartupUiState()),
    firebaseController = firebaseController,
    screenName = "Startup",
) {

    override fun handleEvent(event: StartupEvent) {
        when (event) {
            StartupEvent.RequestConsent -> requestConsent()
            StartupEvent.ConsentFormLoaded -> markConsentFormLoaded()
            StartupEvent.Continue -> sendAction(action = StartupAction.NavigateNext)
        }
    }

    private var consentWatchdogJob: Job? = null

    private fun requestConsent() {
        startOperation(action = Actions.REQUEST_CONSENT)
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setLoading()
            }
            sendAction(StartupAction.RequestConsentUi)
            startConsentWatchdog()
        }
    }

    /**
     * Guarantees the screen stops loading.
     *
     * This is the app's first screen and the only way off it is the button the loading state hides,
     * so a consent round trip that never reports back leaves the user with nowhere to go. Waiting
     * has a limit, after which the screen settles exactly as it does when consent fails.
     */
    private fun startConsentWatchdog() {
        consentWatchdogJob?.cancel()
        consentWatchdogJob = viewModelScope.launch {
            delay(CONSENT_TIMEOUT)
            if (screenState.value.screenState is ScreenState.IsLoading) {
                markConsentFormLoaded()
            }
        }
    }

    private fun markConsentFormLoaded() {
        consentWatchdogJob?.cancel()
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.successData { copy(consentFormLoaded = true) }
            }
        }
    }

    private object Actions {
        const val REQUEST_CONSENT: String = "requestConsent"
    }

    private companion object {
        /** How long the startup screen waits for consent before letting the user carry on. */
        val CONSENT_TIMEOUT: Duration = 15.seconds
    }
}

