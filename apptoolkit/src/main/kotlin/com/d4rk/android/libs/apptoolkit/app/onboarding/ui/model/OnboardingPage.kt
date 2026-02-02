package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OnboardingPage {

    data class DefaultPage(
        val key: String,
        val title: String,
        val description: String,
        val imageVector: ImageVector,
        val isEnabled: Boolean = true
    ) : OnboardingPage()

    /**
     * Represents a custom onboarding page whose content can react to selection state.
     *
     * @property content Composable content for the page, notified when it is the active page.
     */
    data class CustomPage(
        val key: String,
        val content: @Composable (isSelected: Boolean) -> Unit,
        val isEnabled: Boolean = true
    ) : OnboardingPage()
}
