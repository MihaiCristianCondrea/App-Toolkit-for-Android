package com.d4rk.android.apps.apptoolkit.app.main.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface MainEvent : UiEvent {
    data object LoadNavigation : MainEvent
}