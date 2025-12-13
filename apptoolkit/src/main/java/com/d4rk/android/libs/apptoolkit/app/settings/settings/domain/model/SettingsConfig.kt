package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsConfig(
    val title: String = "Settings",
    val categories: List<SettingsCategory> = emptyList(),
)
