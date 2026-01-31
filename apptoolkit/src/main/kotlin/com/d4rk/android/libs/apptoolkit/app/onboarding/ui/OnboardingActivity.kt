package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : ComponentActivity() {

    private val viewModel: OnboardingViewModel by viewModel()
    private val consentHost: ConsentHost = object : ConsentHost {
        override val activity = this@OnboardingActivity
    }
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            checkUserConsent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lifecycleObserver)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                OnboardingScreen()
            }
        }
    }

    private fun checkUserConsent() {
        viewModel.onEvent(OnboardingEvent.RequestConsent(host = consentHost))
    }
}
