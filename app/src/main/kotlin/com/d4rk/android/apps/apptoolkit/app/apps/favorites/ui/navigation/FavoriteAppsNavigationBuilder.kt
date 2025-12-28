package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.navigation

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder

fun favoriteAppsEntryBuilder(
    context: AppNavigationEntryContext,
): NavigationEntryBuilder<AppNavKey> = {
    entry<FavoriteAppsRoute> {
        FavoriteAppsRoute(
            paddingValues = context.paddingValues,
            windowWidthSizeClass = context.windowWidthSizeClass,
            onRegisterRandomAppHandler = context.registerRandomAppHandlerFor(FavoriteAppsRoute),
        )
    }
}
