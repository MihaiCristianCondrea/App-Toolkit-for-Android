package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import com.d4rk.android.libs.apptoolkit.core.utils.platform.ConsentFormHelper
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {

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
        lifecycleScope.launch {
            val consentInfo: ConsentInformation =
                UserMessagingPlatform.getConsentInformation(this@OnboardingActivity)
            ConsentFormHelper.showConsentFormIfRequired(
                activity = this@OnboardingActivity,
                consentInfo = consentInfo
            )
        }
    }
}
