package com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute as AppsListKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute as FavoriteAppsKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.toNavKeyOrDefault
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsActivity
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberDialogSceneStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    navigator: Navigator,
    paddingValues: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass,
    onRandomAppHandlerChanged: (NavKey, RandomAppHandler?) -> Unit,
    startRoute: NavKey,
) {
    val registerAppsListHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(AppsListKey, handler) }
    }
    val registerFavoritesHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(FavoriteAppsKey, handler) }
    }

    val entryProvider = entryProvider<NavKey> {
        entry<AppsListKey> {
            AppsListRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerAppsListHandler,
            )
        }
        entry<FavoriteAppsKey> {
            FavoriteAppsRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerFavoritesHandler,
            )
        }
    }

    NavDisplay(
        modifier = modifier,
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        sceneStrategy = rememberDialogSceneStrategy(startRoute = startRoute)
    )
}

@VisibleForTesting
internal fun DataStore.startupDestinationFlow(): Flow<NavKey> =
    getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST).map { route ->
        route.ifBlank { NavigationRoutes.ROUTE_APPS_LIST }.toNavKeyOrDefault()
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
