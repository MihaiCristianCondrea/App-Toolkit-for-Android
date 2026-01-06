package com.d4rk.android.libs.apptoolkit.app.about.ui.state

import androidx.compose.runtime.Immutable

/**
 * UI representation for the about screen.
 *
 * Values are loaded by [AboutViewModel] using the provided data sources and are
 * exposed as immutable properties to the UI layer.
 */
@Immutable
data class AboutUiState(
    val appVersion: String = "", // TODO: for that we have the AppVersionInfo class
    val appVersionCode: Int = 0, // TODO: for that we have the AppVersionInfo class
    val deviceInfo: String = "",
)