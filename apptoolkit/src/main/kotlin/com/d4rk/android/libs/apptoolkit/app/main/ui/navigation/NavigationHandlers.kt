/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.app.main.ui.navigation

import android.content.Context
import androidx.compose.material3.DrawerState
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsActivity
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openActivity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.shareApp
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
        NavigationDrawerRoutes.ROUTE_SETTINGS -> context.openActivity(
            SettingsActivity::class.java
        ).let { true }

        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> context.openActivity(
            HelpActivity::class.java
        ).let { true }

        NavigationDrawerRoutes.ROUTE_UPDATES -> onChangelogRequested().let { true }
        NavigationDrawerRoutes.ROUTE_SHARE -> context.shareApp(
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
