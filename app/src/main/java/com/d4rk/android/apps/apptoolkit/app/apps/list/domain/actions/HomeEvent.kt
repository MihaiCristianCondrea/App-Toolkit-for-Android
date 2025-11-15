package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.actions

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed class HomeEvent : UiEvent {
    data object FetchApps : HomeEvent()
    data object OpenRandomApp : HomeEvent()
}