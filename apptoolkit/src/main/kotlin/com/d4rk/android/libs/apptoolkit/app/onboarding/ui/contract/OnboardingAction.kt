package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

sealed interface OnboardingAction : ActionEvent {
    data object OnboardingCompleted : OnboardingAction
}
