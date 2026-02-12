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

package com.d4rk.android.libs.apptoolkit.app.startup.ui

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
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupAction
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupEvent
import com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.STARTUP_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartupActivity : BaseActivity() {
    private val provider: StartupProvider by inject()
    private val viewModel: StartupViewModel by viewModel()
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }
    private val consentHost: ConsentHost = object : ConsentHost {
        override val activity = this@StartupActivity
    }

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
                            StartupAction.NavigateNext -> navigateToNext()
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (provider.requiredPermissions.isNotEmpty()) {
                    permissionLauncher.launch(provider.requiredPermissions)
                }
                checkUserConsent()
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
        viewModel.onEvent(StartupEvent.RequestConsent(host = consentHost))
    }
}
