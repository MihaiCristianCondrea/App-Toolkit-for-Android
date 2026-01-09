package com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * Defines UI events that can be triggered from the Advanced Settings screen.
 * These events are sent from the UI to the ViewModel to signal user actions.
 */
sealed interface AdvancedSettingsEvent : UiEvent {
    data object ClearCache : AdvancedSettingsEvent
    data object MessageShown : AdvancedSettingsEvent
}
