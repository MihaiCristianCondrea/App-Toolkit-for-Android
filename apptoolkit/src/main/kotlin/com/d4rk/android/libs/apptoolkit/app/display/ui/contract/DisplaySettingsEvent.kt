package com.d4rk.android.libs.apptoolkit.app.display.ui.contract

/** Events emitted by the display settings screen. */
sealed interface DisplaySettingsEvent {
    data object Load : DisplaySettingsEvent
}
