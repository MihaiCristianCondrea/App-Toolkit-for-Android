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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : ComponentActivity() {

    private val viewModel: OnboardingViewModel by viewModel()
    private val requestConsentUseCase: RequestConsentUseCase by inject()
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            checkUserConsent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lifecycleObserver)
        observeActions()
        enableEdgeToEdge()
        setContent {
            AppTheme {
                OnboardingScreen()
            }
        }
    }

    private fun checkUserConsent() {
        viewModel.onEvent(OnboardingEvent.RequestConsent)
    }

    private fun observeActions() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionEvent.collect { action ->
                    when (action) {
                        OnboardingAction.RequestConsentUi -> requestConsentFromUi()
                        OnboardingAction.OnboardingCompleted -> Unit
                    }
                }
            }
        }
    }

    private fun requestConsentFromUi() {
        val host: ConsentHost = object : ConsentHost {
            override val activity = this@OnboardingActivity
        }
        lifecycleScope.launch {
            requestConsentUseCase.invoke(host = host).collect { }
        }
    }
}
