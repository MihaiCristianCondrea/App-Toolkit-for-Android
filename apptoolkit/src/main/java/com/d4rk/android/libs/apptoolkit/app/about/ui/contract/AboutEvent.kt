package com.d4rk.android.libs.apptoolkit.app.about.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * User driven events from the About screen UI.
 */
sealed interface AboutEvent : UiEvent {
    /**
     * Copies the device information to the clipboard.
     */
    data class CopyDeviceInfo(val label: String) : AboutEvent

    /**
     * Dismisses the currently displayed snackbar.
     */
    data object DismissSnackbar : AboutEvent
}
