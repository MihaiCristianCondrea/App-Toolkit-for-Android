package com.d4rk.android.libs.apptoolkit.core.domain.model.theme

/**
 * Represents the persisted theme-related preferences exposed to the UI layer.
 */
data class ThemePreferencesState(
    val themeMode: String,
    val dynamicColors: Boolean,
    val amoledMode: Boolean,
    val dynamicPaletteVariant: Int,
    val staticPaletteId: String,
)
