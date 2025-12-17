package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Star
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object MainNavigationDefaults {
    val fabSupportedRoutes: Set<String> = setOf(
        NavigationRoutes.ROUTE_APPS_LIST,
        NavigationRoutes.ROUTE_FAVORITE_APPS
    )

    val bottomBarItems: ImmutableList<BottomBarItem> = persistentListOf(
        BottomBarItem(
            route = NavigationRoutes.ROUTE_APPS_LIST,
            icon = Icons.Outlined.Apps,
            selectedIcon = Icons.Rounded.Apps,
            title = R.string.all_apps
        ),
        BottomBarItem(
            route = NavigationRoutes.ROUTE_FAVORITE_APPS,
            icon = Icons.Outlined.StarOutline,
            selectedIcon = Icons.Rounded.Star,
            title = R.string.favorite_apps
        )
    )
}

internal fun String?.normalizeRoute(): String? = this
    ?.substringBefore('?')
    ?.substringBefore('/')
    ?.takeIf { it.isNotBlank() }
