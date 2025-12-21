package com.d4rk.android.libs.apptoolkit.app.startup.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

sealed interface StartupAction : ActionEvent {
    data object NavigateNext : StartupAction
}
