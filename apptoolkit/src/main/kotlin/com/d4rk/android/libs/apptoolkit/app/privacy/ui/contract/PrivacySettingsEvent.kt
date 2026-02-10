package com.d4rk.android.libs.apptoolkit.app.privacy.ui.contract

/** Events emitted by the privacy settings screen. */
sealed interface PrivacySettingsEvent {
    data object Load : PrivacySettingsEvent
}
