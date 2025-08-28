package com.d4rk.android.libs.apptoolkit.app.advanced.domain.actions

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface AdvancedSettingsEvent : UiEvent {
    data object ClearCache : AdvancedSettingsEvent
    data object MessageShown : AdvancedSettingsEvent
}
