package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface OnboardingEvent : UiEvent {
    data class UpdateCurrentTab(val index: Int) : OnboardingEvent
    data class RequestConsent(val host: ConsentHost) : OnboardingEvent
    data object CompleteOnboarding : OnboardingEvent
    data object ShowCrashlyticsDialog : OnboardingEvent
    data object HideCrashlyticsDialog : OnboardingEvent
}
