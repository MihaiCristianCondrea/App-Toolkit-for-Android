package com.d4rk.android.apps.apptoolkit.app.components.ui.navigation

import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.apps.apptoolkit.app.components.ui.views.ComponentsRoute as ComponentsScreenRoute

/**
 * Registers the components showcase entry in the app navigation graph.
 */
fun componentsEntryBuilder(
    context: AppNavigationEntryContext,
): NavigationEntryBuilder<AppNavKey> = {
    entry<ComponentsRoute> {
        ComponentsScreenRoute(
            paddingValues = context.paddingValues,
        )
    }
}
