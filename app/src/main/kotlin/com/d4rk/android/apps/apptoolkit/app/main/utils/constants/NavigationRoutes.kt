package com.d4rk.android.apps.apptoolkit.app.main.utils.constants

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AppsListRoute : NavKey

@Serializable
data object FavoriteAppsRoute : NavKey

object NavigationRoutes {
    const val ROUTE_APPS_LIST: String = "apps_list"
    const val ROUTE_FAVORITE_APPS: String = "favorite_apps"

    val topLevelRoutes: Set<NavKey> = setOf(AppsListRoute, FavoriteAppsRoute)
}

fun String.toNavKeyOrDefault(): NavKey =
    when (this) {
        NavigationRoutes.ROUTE_FAVORITE_APPS -> FavoriteAppsRoute
        else -> AppsListRoute
    }
