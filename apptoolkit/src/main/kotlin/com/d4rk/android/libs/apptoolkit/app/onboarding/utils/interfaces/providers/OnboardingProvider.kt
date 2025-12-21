package com.d4rk.android.libs.apptoolkit.app.onboarding.utils.interfaces.providers

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.data.model.ui.OnboardingPage

interface OnboardingProvider {
    fun getOnboardingPages(context: Context): List<OnboardingPage>
    fun onOnboardingFinished(context: Context)
}