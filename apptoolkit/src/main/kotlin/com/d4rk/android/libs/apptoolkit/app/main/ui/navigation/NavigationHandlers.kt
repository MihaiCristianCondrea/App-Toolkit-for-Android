package com.d4rk.android.libs.apptoolkit.app.main.ui.navigation

import android.content.Context
import androidx.compose.material3.DrawerState
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsActivity
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Handles clicks coming from the navigation drawer.
 *
 * Exposed from the library so applications can easily reuse the same behavior
 * while still customizing the changelog handling callback.
 */
fun handleNavigationItemClick(
    context: Context,
    item: NavigationDrawerItem,
    drawerState: DrawerState? = null,
    coroutineScope: CoroutineScope? = null,
    onChangelogRequested: () -> Unit = {},
    additionalHandlers: Map<String, (NavigationDrawerItem) -> Unit> = emptyMap(),
) {
    val handled = when (item.route) {
        NavigationDrawerRoutes.ROUTE_SETTINGS -> IntentsHelper.openActivity(
            context = context,
            activityClass = SettingsActivity::class.java
        ).let { true }

        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> IntentsHelper.openActivity(
            context = context,
            activityClass = HelpActivity::class.java
        ).let { true }

        NavigationDrawerRoutes.ROUTE_UPDATES -> onChangelogRequested().let { true }
        NavigationDrawerRoutes.ROUTE_SHARE -> IntentsHelper.shareApp(
            context = context,
            shareMessageFormat = com.d4rk.android.libs.apptoolkit.R.string.summary_share_message
        ).let { true }

        else -> additionalHandlers[item.route]?.let { handler ->
            handler(item)
            true
        } ?: false
    }
    if (handled && drawerState != null && coroutineScope != null) {
        coroutineScope.launch { drawerState.close() }
    }
}
