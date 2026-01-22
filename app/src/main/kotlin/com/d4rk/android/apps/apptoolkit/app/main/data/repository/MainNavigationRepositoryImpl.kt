package com.d4rk.android.apps.apptoolkit.app.main.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Widgets
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.d4rk.android.libs.apptoolkit.R as ToolkitR

class MainNavigationRepositoryImpl(
    private val dataStore: DataStore,
) : NavigationRepository {
    override fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>> =
        dataStore.componentsShowcaseUnlocked.map { isUnlocked ->
            buildList {
                if (BuildConfig.DEBUG || isUnlocked) {
                    add(
                        NavigationDrawerItem(
                            title = R.string.components_title,
                            selectedIcon = Icons.Outlined.Widgets,
                            route = NavigationRoutes.ROUTE_COMPONENTS,
                        )
                    )
                }
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.settings,
                        selectedIcon = Icons.Outlined.Settings,
                        route = NavigationDrawerRoutes.ROUTE_SETTINGS,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.help_and_feedback,
                        selectedIcon = Icons.AutoMirrored.Outlined.HelpOutline,
                        route = NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.updates,
                        selectedIcon = Icons.AutoMirrored.Outlined.EventNote,
                        route = NavigationDrawerRoutes.ROUTE_UPDATES,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.share,
                        selectedIcon = Icons.Outlined.Share,
                        route = NavigationDrawerRoutes.ROUTE_SHARE,
                    ),
                )
            }
        }
}
