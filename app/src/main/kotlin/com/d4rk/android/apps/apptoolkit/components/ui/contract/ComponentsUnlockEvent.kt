package com.d4rk.android.apps.apptoolkit.components.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.state.UiEvent

/**
 * Events for unlocking the components showcase entry.
 */
sealed interface ComponentsUnlockEvent : UiEvent {
    /** Initialization event fired from the ViewModel init block. */
    data object Initialize : ComponentsUnlockEvent

    /** User tapped the app version in the About screen. */
    data class VersionTapped(val tapCount: Int) : ComponentsUnlockEvent
}
