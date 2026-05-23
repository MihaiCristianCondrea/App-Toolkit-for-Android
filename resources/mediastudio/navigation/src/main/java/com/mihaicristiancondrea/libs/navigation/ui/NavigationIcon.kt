package com.mihaicristiancondrea.libs.navigation.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.mihaicristiancondrea.libs.navigation.models.NavigationItemIcon

@Composable
fun NavigationItemIcon.NavigationIcon(
    contentDescription: String?,
) {
    when (this) {
        is NavigationItemIcon.Vector -> {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }

        is NavigationItemIcon.PainterIcon -> {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
            )
        }
    }
}
