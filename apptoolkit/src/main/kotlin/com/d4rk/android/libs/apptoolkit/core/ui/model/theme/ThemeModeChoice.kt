package com.d4rk.android.libs.apptoolkit.core.ui.model.theme

import androidx.compose.ui.graphics.vector.ImageVector

data class ThemeModeChoice(
    val key: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
)