package com.d4rk.android.libs.apptoolkit.app.about.ui.mapper

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * Extension function to map [AboutInfo] domain model to [AboutUiState] UI state.
 *
 * @return A new instance of [AboutUiState] containing the mapped application and device information.
 */
internal fun AboutInfo.toUiState(): AboutUiState =
    AboutUiState(
        appVersionInfo = AppVersionInfo(
            versionName = appVersion,
            versionCode = appVersionCode.toLong()
        ),
        deviceInfo = deviceInfo,
    )
