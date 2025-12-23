package com.d4rk.android.apps.apptoolkit.app.main.utils.constants

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.StableNavKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable

@Immutable
sealed interface AppNavKey : StableNavKey

@Serializable
data object AppsListRoute : AppNavKey

@Serializable
data object FavoriteAppsRoute : AppNavKey

object NavigationRoutes {
    const val ROUTE_APPS_LIST: String = "apps_list"
    const val ROUTE_FAVORITE_APPS: String = "favorite_apps"

    val topLevelRoutes: ImmutableSet<AppNavKey> = persistentSetOf(AppsListRoute, FavoriteAppsRoute)
}

fun String.toNavKeyOrDefault(): AppNavKey =
    when (this) {
        NavigationRoutes.ROUTE_FAVORITE_APPS -> FavoriteAppsRoute
        else -> AppsListRoute
    }
