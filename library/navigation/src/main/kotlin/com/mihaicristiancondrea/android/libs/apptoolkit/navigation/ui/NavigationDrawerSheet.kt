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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

/**
 * Displays a navigation drawer sheet containing a list of [NavigationDrawerItem]s.
 *
 * A drawer holding nothing but the standard Settings, Help, Updates and Share entries renders as
 * one block, top-aligned. As soon as the host adds a destination of its own, those standard entries
 * stop being the drawer's content and become its footer: the app's own destinations take the top,
 * the standard ones are pushed to the bottom edge, and [branding] — when the host supplies it —
 * names the app above them. That is the rule [pinnedRoutes] states; pass an empty set to turn it
 * off and render [items] in the order given.
 *
 * The footer is pinned with a weighted spacer, so a drawer whose entries are taller than the screen
 * will clip rather than scroll. Keep the two groups together short enough to fit, which the
 * standard entries plus a handful of app destinations are.
 *
 * @param items A list of [NavigationDrawerItem]s to render.
 * @param modifier The [Modifier] to be applied to the [ModalDrawerSheet].
 * @param drawerState State of the drawer sheet.
 * @param currentRoute The route of the currently displayed destination, used for selection if [isSelected] is not provided.
 * @param dividerRoutes A set of route strings after which a horizontal divider should be drawn.
 * @param branding The app's logo and name, drawn above its own destinations once there are any.
 * Null leaves the drawer unbranded, which is what a drawer of only standard entries wants.
 * @param pinnedRoutes The routes that drop to the bottom once [items] carries anything outside
 * them. Defaults to [NavigationDrawerRoutes.StandardRoutes].
 * @param isSelected A lambda to determine whether a given item is currently selected.
 * @param onItemClick A lambda to handle item click events.
 * @param headerContent Optional content placed at the top of the drawer sheet, above [branding],
 * defaults to a vertical spacer.
 */
@Composable
fun NavigationDrawerSheet(
    items: ImmutableList<NavigationDrawerItem>,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    currentRoute: String? = null,
    dividerRoutes: ImmutableSet<String> = persistentSetOf(),
    branding: NavigationDrawerBranding? = null,
    pinnedRoutes: ImmutableSet<String> = NavigationDrawerRoutes.StandardRoutes,
    isSelected: (NavigationDrawerItem) -> Boolean = { item -> item.route == currentRoute },
    onItemClick: (NavigationDrawerItem) -> Unit = {},
    headerContent: (@Composable ColumnScope.() -> Unit)? = {
        Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
    },
) {
    val appItems: List<NavigationDrawerItem> = remember(items, pinnedRoutes) {
        items.filterNot { item -> item.route in pinnedRoutes }
    }
    val footerItems: List<NavigationDrawerItem> = remember(items, pinnedRoutes, appItems) {
        if (appItems.isEmpty()) items else items.filter { item -> item.route in pinnedRoutes }
    }

    ModalDrawerSheet(
        modifier = modifier,
        drawerState = drawerState,
    ) {
        headerContent?.invoke(this)

        if (appItems.isNotEmpty() && branding != null) {
            NavigationDrawerHeader(branding = branding)
            Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
        }

        appItems.forEach { item ->
            DrawerItem(
                item = item,
                isSelected = isSelected,
                dividerRoutes = dividerRoutes,
                onItemClick = onItemClick,
            )
        }

        if (appItems.isNotEmpty()) {
            Spacer(modifier = Modifier.weight(weight = 1f))
        }

        footerItems.forEach { item ->
            DrawerItem(
                item = item,
                isSelected = isSelected,
                dividerRoutes = dividerRoutes,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun DrawerItem(
    item: NavigationDrawerItem,
    isSelected: (NavigationDrawerItem) -> Boolean,
    dividerRoutes: ImmutableSet<String>,
    onItemClick: (NavigationDrawerItem) -> Unit,
) {
    NavigationDrawerItemContent(
        item = item,
        selected = isSelected(item),
        dividerRoutes = dividerRoutes,
    ) {
        onItemClick(item)
    }
}
