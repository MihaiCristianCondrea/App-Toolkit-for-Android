package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

sealed interface HomeAction : ActionEvent {
    data class OpenRandomApp(val app: AppInfo) : HomeAction
}

