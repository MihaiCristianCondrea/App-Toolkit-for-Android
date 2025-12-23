package com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute as AppsListKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute as FavoriteAppsKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.toEntries

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navigationState: NavigationState<AppNavKey>,
    navigator: Navigator<AppNavKey>,
    paddingValues: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass,
    onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit,
) {
    val registerAppsListHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(AppsListKey, handler) }
    }
    val registerFavoritesHandler = remember(onRandomAppHandlerChanged) {
        { handler: RandomAppHandler? -> onRandomAppHandlerChanged(FavoriteAppsKey, handler) }
    }

    val entryProvider = entryProvider {
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
        transitionSpec = { NavigationAnimations.default() },
        popTransitionSpec = { NavigationAnimations.default() },
        predictivePopTransitionSpec = { NavigationAnimations.default() },
    )
}
