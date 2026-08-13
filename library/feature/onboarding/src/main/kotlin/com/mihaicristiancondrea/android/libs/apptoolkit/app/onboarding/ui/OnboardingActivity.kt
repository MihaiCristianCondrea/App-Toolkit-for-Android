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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.BaseActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.activity.observeActions
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : BaseActivity() {

    private val viewModel: OnboardingViewModel by viewModel()
    private val consentRepository: ConsentRepository by inject()
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            checkUserConsent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lifecycleObserver)
        observeActions()
    }

    @Composable
    override fun ScreenContent() {
        OnboardingScreen()
    }

    private fun checkUserConsent() {
        viewModel.onEvent(OnboardingEvent.RequestConsent)
    }

    private fun observeActions() {
        observeActions(viewModel = viewModel) { action ->
            when (action) {
                OnboardingAction.RequestConsentUi -> requestConsentFromUi()
                OnboardingAction.OnboardingCompleted -> Unit
            }
        }
    }

    private fun requestConsentFromUi() {
        val host: ConsentHost = object : ConsentHost {
            override val activity = this@OnboardingActivity
        }
        lifecycleScope.launch {
            consentRepository.requestConsent(host = host).collect { }
        }
    }
}

