package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.contract

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

sealed interface FavoriteAppsAction : ActionEvent {
    data class OpenRandomApp(val app: AppInfo) : FavoriteAppsAction
}

