package com.d4rk.android.libs.apptoolkit.app.ads.ui.contract

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/** User interactions on the ads settings screen. */
sealed interface AdsSettingsEvent : UiEvent {
    data object Initialize : AdsSettingsEvent
    data class SetAdsEnabled(val enabled: Boolean) : AdsSettingsEvent
    data class RequestConsent(val host: ConsentHost) : AdsSettingsEvent
}
