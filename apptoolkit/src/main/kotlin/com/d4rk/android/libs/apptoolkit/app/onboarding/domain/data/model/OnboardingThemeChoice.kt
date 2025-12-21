package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingThemeChoice(
    val key: String, val displayName: String, val icon: ImageVector, val description: String
)