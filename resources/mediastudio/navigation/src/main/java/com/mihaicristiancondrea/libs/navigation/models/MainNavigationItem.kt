package com.mihaicristiancondrea.libs.navigation.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
sealed interface NavigationItemIcon {

    @Immutable
    data class Vector(
        val imageVector: ImageVector,
    ) : NavigationItemIcon

    @Immutable
    data class PainterIcon(
        val painter: Painter,
    ) : NavigationItemIcon
}

@Immutable
data class MainNavigationItem<T : NavigationDestination>(
    val destination: T,
    val title: String,
    val shortTitle: String,
    val icon: NavigationItemIcon,
    val selectedIcon: NavigationItemIcon? = null,
    val badgeText: String? = null,
)
