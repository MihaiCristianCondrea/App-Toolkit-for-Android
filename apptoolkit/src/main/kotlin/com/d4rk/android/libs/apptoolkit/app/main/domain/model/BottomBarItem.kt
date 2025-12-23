package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.navigation.StableNavKey

@Immutable
data class BottomBarItem(
    val route: StableNavKey,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
)
