package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsCategory(
    val title: String = "",
    val preferences: List<SettingsPreference> = emptyList(),
)