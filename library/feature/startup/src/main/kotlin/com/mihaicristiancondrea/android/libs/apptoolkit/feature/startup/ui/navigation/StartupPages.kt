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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.navigation

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.StartupScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.StartupViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts.StartupAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.OnboardingRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.StartupRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * Registers [StartupRoute], the first-launch start screen (consent and runtime permissions),
 * unless the app registered its own. Continuing hands over to [OnboardingRoute], which
 * `:library:feature:onboarding` registers; this module opens it by key and does not depend on it.
 *
 * There is no shell under it, so back leaves the app. The app opens it by starting on
 * [StartupRoute] until onboarding is done, from `ShellHost(resolveStart = ...)`:
 *
 * ```
 * resolveStart = { if (dataStore.startup.first()) StartupRoute else null }
 * ```
 *
 * It is also offered as a start screen in the developer options, to try it again.
 */
fun ShellGraphBuilder.startupPage() {
    pageIfAbsent<StartupRoute>(paneRole = PaneRole.None, title = null) { StartupPage() }
    startScreens(StartupRoute)
}

/**
 * The startup screen. On its first resume it asks for [StartupProvider.requiredPermissions], and
 * on every resume until consent has resolved it asks for consent; continuing hands over to
 * onboarding.
 */
@Composable
private fun StartupPage() {
    val viewModel: StartupViewModel = koinViewModel()
    val provider: StartupProvider = koinInject()
    val consentRepository: ConsentRepository = koinInject()
    val navigator = LocalShellNavigator.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { }
    // Saved, so the resume that follows the system permission dialog does not ask again: a person
    // who declined was otherwise asked again on the spot.
    var hasRequestedPermissions by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                StartupAction.RequestConsentUi -> {
                    val host = activity?.let(::ConsentHost) ?: return@collect
                    scope.launch {
                        consentRepository.requestConsent(host = host).collect { result ->
                            if (result !is DataState.Loading) {
                                viewModel.onEvent(StartupEvent.ConsentFormLoaded)
                            }
                        }
                    }
                }

                StartupAction.NavigateNext -> navigator.continueStart(OnboardingRoute)
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        if (!hasRequestedPermissions && provider.requiredPermissions.isNotEmpty()) {
            hasRequestedPermissions = true
            permissionLauncher.launch(provider.requiredPermissions)
        }
        // Consent is asked for again on a later resume only while it has not resolved, which
        // recovers a failed round trip without sending the screen back to its loading state.
        if (viewModel.uiState.value.data?.consentFormLoaded != true) {
            viewModel.onEvent(StartupEvent.RequestConsent)
        }
        onPauseOrDispose { }
    }

    StartupScreen(
        screenState = screenState,
        onContinueClick = { viewModel.onEvent(StartupEvent.Continue) },
    )
}
