package com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.navigation.favoriteAppsEntryBuilder
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.navigation.appsListEntryBuilder
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.components.ui.navigation.componentsEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass

/**
 * Context shared by all navigation entry builders in the app module.
 */
@Stable
data class AppNavigationEntryContext(
    val paddingValues: PaddingValues,
    val windowWidthSizeClass: AppWindowWidthSizeClass,
    val onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit,
) {
    fun registerRandomAppHandlerFor(route: AppNavKey): (RandomAppHandler?) -> Unit = { handler ->
        onRandomAppHandlerChanged(route, handler)
    }
}

/**
 * Default app navigation builders that can be extended with additional entries.
 */
fun appNavigationEntryBuilders(
    context: AppNavigationEntryContext,
    additionalEntryBuilders: List<NavigationEntryBuilder<AppNavKey>> = emptyList(),
): List<NavigationEntryBuilder<AppNavKey>> = buildList {
    addAll(defaultAppNavigationEntryBuilders(context))
    addAll(additionalEntryBuilders)
}

private fun defaultAppNavigationEntryBuilders(
    context: AppNavigationEntryContext
): List<NavigationEntryBuilder<AppNavKey>> = listOf(
    appsListEntryBuilder(context),
    favoriteAppsEntryBuilder(context),
    componentsEntryBuilder(context),
)
