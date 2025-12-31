package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.navigation

import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder

fun appsListEntryBuilder(
    context: AppNavigationEntryContext,
): NavigationEntryBuilder<AppNavKey> = {
    entry<AppsListRoute> {
        AppsListRoute(
            paddingValues = context.paddingValues,
            windowWidthSizeClass = context.windowWidthSizeClass,
            onRegisterRandomAppHandler = context.registerRandomAppHandlerFor(AppsListRoute),
        )
    }
}
