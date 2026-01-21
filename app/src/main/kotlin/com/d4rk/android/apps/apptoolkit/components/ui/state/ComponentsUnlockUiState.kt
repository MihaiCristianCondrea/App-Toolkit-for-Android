package com.d4rk.android.apps.apptoolkit.components.ui.state

/**
 * UI state for the components unlock flow.
 */
data class ComponentsUnlockUiState(
    val isUnlocked: Boolean = false,
    val lastTapCount: Int = 0,
)
