package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Star
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import androidx.navigation3.runtime.NavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// TODO && FIXME: Move somewhere else to fit architecture
internal object MainNavigationDefaults {
    val fabSupportedRoutes: Set<NavKey> = setOf(
        AppsListRoute,
        FavoriteAppsRoute
    )

    val bottomBarItems: ImmutableList<BottomBarItem> = persistentListOf(
        BottomBarItem(
            route = AppsListRoute,
            icon = Icons.Outlined.Apps,
            selectedIcon = Icons.Rounded.Apps,
            title = R.string.all_apps
        ),
        BottomBarItem(
            route = FavoriteAppsRoute,
            icon = Icons.Outlined.StarOutline,
            selectedIcon = Icons.Rounded.Star,
            title = R.string.favorite_apps
        )
    )
}
