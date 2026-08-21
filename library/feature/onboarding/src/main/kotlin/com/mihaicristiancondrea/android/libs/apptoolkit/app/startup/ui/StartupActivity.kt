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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.ui.contracts.StartupAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.ui.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.STARTUP_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.BaseActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartupActivity : BaseActivity() {
    private val provider: StartupProvider by inject()
    private val consentRepository: ConsentRepository by inject()
    private val viewModel: StartupViewModel by viewModel()
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }
    private var hasRequestedPermissions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionEvent
                    .onCompletion { cause: Throwable? ->
                        if (cause != null && cause !is CancellationException) {
                            Log.w(
                                STARTUP_LOG_TAG,
                                "Startup action flow completed with an error.",
                                cause
                            )
                        }
                    }
                    .collect { action: StartupAction ->
                        when (action) {
                            StartupAction.RequestConsentUi -> performConsentRequest()
                            StartupAction.NavigateNext -> navigateToNext()
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // Asking for permissions pauses this activity, so the resume that follows the
                // system dialog used to ask again: a user who declined was re-prompted on the spot.
                if (!hasRequestedPermissions && provider.requiredPermissions.isNotEmpty()) {
                    hasRequestedPermissions = true
                    permissionLauncher.launch(provider.requiredPermissions)
                }

                // Consent is asked for again on a later resume only while it has not resolved,
                // which recovers a failed round trip without sending the screen back to its loading
                // state every time the user returns to it.
                if (viewModel.uiState.value.data?.consentFormLoaded != true) {
                    checkUserConsent()
                }
            }
        }
    }

    @Composable
    override fun ScreenContent() {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        StartupScreen(
            screenState = screenState,
            onContinueClick = { viewModel.onEvent(StartupEvent.Continue) }
        )
    }

    private fun navigateToNext() {
        openActivity(
            activityClass = provider.getNextIntent(this@StartupActivity)
                .component?.className?.let { Class.forName(it) }
                ?: StartupActivity::class.java
        )
        finish()
    }

    private fun checkUserConsent() {
        viewModel.onEvent(StartupEvent.RequestConsent)
    }

    private fun performConsentRequest() {
        val host: ConsentHost = object : ConsentHost {
            override val activity = this@StartupActivity
        }

        lifecycleScope.launch {
            consentRepository.requestConsent(host = host).collect { result ->
                if (result !is DataState.Loading) {
                    viewModel.onEvent(StartupEvent.ConsentFormLoaded)
                }
            }
        }
    }
}

