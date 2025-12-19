package com.d4rk.android.libs.apptoolkit.app.startup.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface StartupEvent : UiEvent {
    data object ConsentFormLoaded : StartupEvent
    data object Continue : StartupEvent
}
