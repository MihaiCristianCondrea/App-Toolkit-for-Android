package com.d4rk.android.libs.apptoolkit.app.help.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface HelpEvent : UiEvent {
    data object LoadFaq : HelpEvent
    data object DismissSnackbar : HelpEvent
}

