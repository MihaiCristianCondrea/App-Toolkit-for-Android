package com.d4rk.android.apps.apptoolkit.app.main.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.icons.outlined.StarOutline
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainNavigationRepositoryImpl(
    private val dataStore: DataStore,
) : NavigationRepository {
    override fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>> =
        dataStore.componentsShowcaseUnlocked.map { isUnlocked ->
            buildList {
                add(
                    NavigationDrawerItem(
                        title = R.string.all_apps,
                        selectedIcon = Icons.Outlined.Apps,
                        route = NavigationRoutes.ROUTE_APPS_LIST,
                    )
                )
                add(
                    NavigationDrawerItem(
                        title = R.string.favorite_apps,
                        selectedIcon = Icons.Outlined.StarOutline,
                        route = NavigationRoutes.ROUTE_FAVORITE_APPS,
                    )
                )
                if (BuildConfig.DEBUG || isUnlocked) {
                    add(
                        NavigationDrawerItem(
                            title = R.string.components_title,
                            selectedIcon = Icons.Outlined.Widgets,
                            route = NavigationRoutes.ROUTE_COMPONENTS,
                        )
                    )
                }
            }
        }
}
