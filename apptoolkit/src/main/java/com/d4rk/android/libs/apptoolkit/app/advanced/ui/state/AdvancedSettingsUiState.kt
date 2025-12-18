package com.d4rk.android.libs.apptoolkit.app.advanced.ui.state

/**
 * Represents the state for [AdvancedSettingsViewModel].
 *
 * @param cacheClearMessage optional string resource id to show after clearing cache
 */
data class AdvancedSettingsUiState(
    val cacheClearMessage: Int? = null,
)