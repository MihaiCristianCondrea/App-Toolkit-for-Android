package com.d4rk.android.libs.apptoolkit.app.about.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * UI representation for the about screen.
 *
 * Values are loaded by [AboutViewModel] using the provided data sources and are
 * exposed as immutable properties to the UI layer.
 */
@Immutable
data class AboutUiState(
    val appVersionInfo: AppVersionInfo = AppVersionInfo(versionName = "", versionCode = 0L),
    val deviceInfo: String = "",
)
