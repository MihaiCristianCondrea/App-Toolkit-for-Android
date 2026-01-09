package com.d4rk.android.libs.apptoolkit.app.about.ui.mapper

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

internal fun AboutInfo.toUiState(): AboutUiState =
    AboutUiState(
        appVersionInfo = AppVersionInfo(
            versionName = appVersion,
            versionCode = appVersionCode.toLong()
        ),
        deviceInfo = deviceInfo,
    )
