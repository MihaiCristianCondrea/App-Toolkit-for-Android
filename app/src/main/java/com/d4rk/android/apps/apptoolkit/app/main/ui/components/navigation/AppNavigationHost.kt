package com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.composable
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.NavigationHost
import com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation.StableNavController
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsActivity
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AppNavigationHost(
    navController: StableNavController,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass,
    onRandomAppHandlerChanged: (route: String, RandomAppHandler?) -> Unit,
) {
    val dataStore: DataStore = koinInject()

    val startupRoute: String by dataStore
        .startupDestinationFlow()
        .collectAsStateWithLifecycle(initialValue = NavigationRoutes.ROUTE_APPS_LIST)

    val startDestination: String = remember(startupRoute) {
        startupRoute.ifBlank { NavigationRoutes.ROUTE_APPS_LIST }
    }

    val registerAppsListHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? ->
            onRandomAppHandlerChanged(NavigationRoutes.ROUTE_APPS_LIST, handler)
        }
    }
    val registerFavoritesHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? ->
            onRandomAppHandlerChanged(NavigationRoutes.ROUTE_FAVORITE_APPS, handler)
        }
    }

    NavigationHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavigationRoutes.ROUTE_APPS_LIST) {
            AppsListRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerAppsListHandler,
            )
        }
        composable(route = NavigationRoutes.ROUTE_FAVORITE_APPS) {
            FavoriteAppsRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerFavoritesHandler,
            )
        }
    }
}

@VisibleForTesting
internal fun DataStore.startupDestinationFlow(): Flow<String> =
    getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST).map { route ->
        route.ifBlank { NavigationRoutes.ROUTE_APPS_LIST }
    }

fun handleNavigationItemClick(
    context: Context,
    item: NavigationDrawerItem,
    drawerState: DrawerState? = null,
    coroutineScope: CoroutineScope? = null,
    onChangelogRequested: () -> Unit = {},
) {
    when (item.route) {
        NavigationDrawerRoutes.ROUTE_SETTINGS -> IntentsHelper.openActivity(
            context = context,
            activityClass = SettingsActivity::class.java
        )

        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> IntentsHelper.openActivity(
            context = context,
            activityClass = HelpActivity::class.java
        )

        NavigationDrawerRoutes.ROUTE_UPDATES -> onChangelogRequested()
        NavigationDrawerRoutes.ROUTE_SHARE -> IntentsHelper.shareApp(
            context = context,
            shareMessageFormat = com.d4rk.android.libs.apptoolkit.R.string.summary_share_message
        )
    }
    if (drawerState != null && coroutineScope != null) {
        coroutineScope.launch { drawerState.close() }
    }
}

