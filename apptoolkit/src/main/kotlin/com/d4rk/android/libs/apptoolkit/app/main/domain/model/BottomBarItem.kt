package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
)