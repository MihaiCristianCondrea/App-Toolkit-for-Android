package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

@Immutable
data class BottomBarItem(
    val route: NavKey,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
)
