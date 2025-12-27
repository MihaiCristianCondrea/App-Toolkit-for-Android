package com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.d4rk.android.libs.apptoolkit.core.utils.window.AppWindowWidthSizeClass

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navigationState: NavigationState<AppNavKey>,
    navigator: Navigator<AppNavKey>,
    paddingValues: PaddingValues,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit,
) {
    val registerAppsListHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(AppsListRoute, handler) }
    }
    val registerFavoritesHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(FavoriteAppsRoute, handler) }
    }

    val entryProvider = entryProvider {
        entry<AppsListRoute> {
            AppsListRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerAppsListHandler,
            )
        }
        entry<FavoriteAppsRoute> {
            FavoriteAppsRoute(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRegisterRandomAppHandler = registerFavoritesHandler,
            )
        }
    }

    val entryDecorators = rememberNavigationEntryDecorators<AppNavKey>()

    NavDisplay(
        modifier = modifier,
        backStack = navigationState.currentBackStack,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider,
        onBack = { navigator.goBack() },
        transitionSpec = { NavigationAnimations.default() },
        popTransitionSpec = { NavigationAnimations.default() },
        predictivePopTransitionSpec = { NavigationAnimations.default() },
    )
}
