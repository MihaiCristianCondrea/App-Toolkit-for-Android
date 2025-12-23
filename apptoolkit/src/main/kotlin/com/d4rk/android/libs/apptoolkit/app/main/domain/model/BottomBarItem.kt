package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.StableNavKey

@Immutable
data class BottomBarItem<T : StableNavKey>(
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
)
