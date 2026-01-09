package com.d4rk.android.libs.apptoolkit.app.about.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * User driven events from the About screen UI.
 */
sealed interface AboutEvent : UiEvent {
    data object Load : AboutEvent
    data class CopyDeviceInfo(val label: String) : AboutEvent
    data object DismissSnackbar : AboutEvent
}
