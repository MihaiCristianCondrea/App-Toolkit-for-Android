package com.d4rk.android.libs.apptoolkit.app.about.ui.mapper

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState

internal fun AboutInfo.toUiState(): AboutUiState =
        AboutUiState(
            appVersion = appVersion,
            appVersionCode = appVersionCode,
            deviceInfo = deviceInfo,
        )
