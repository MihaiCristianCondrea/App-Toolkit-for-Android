package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state

/**
 * UI state for [com.d4rk.android.libs.apptoolkit.app.onboarding.ui.OnboardingViewModel].
 */
data class OnboardingUiState(
    val currentTabIndex: Int = 0,
    val isOnboardingCompleted: Boolean = false,
    val isCrashlyticsDialogVisible: Boolean = true,
)
