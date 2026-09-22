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
import androidx.compose.runtime.Immutable
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
 * Two behaviours sit on top of the plain list, and they are independent of each other.
 *
 * [branding] draws the app's logo and name above the items. It is off unless a host passes one, so a
 * drawer that does not name its app renders exactly as it did before the header existed.
 *
 * [pinStandardRoutes] moves the entries named by [pinnedRoutes], the standard Settings, Help,
 * Updates and Share destinations, to the bottom edge of the drawer once the host adds a destination
 * of its own. It is on by default. A drawer holding nothing but those standard entries is unchanged
 * either way, because there is nothing to separate them from. Pass `false` to render [items] in the
 * order given.
 *
 * The footer is pinned with a weighted spacer, so a drawer whose entries are taller than the screen
 * will clip rather than scroll. Keep the two groups together short enough to fit, which the standard
 * entries plus a handful of app destinations are.
 *
 * @param items A list of [NavigationDrawerItem]s to render.
 * @param modifier The [Modifier] to be applied to the [ModalDrawerSheet].
 * @param drawerState State of the drawer sheet.
 * @param currentRoute The route of the currently displayed destination, used for selection if [isSelected] is not provided.
 * @param dividerRoutes A set of route strings after which a horizontal divider should be drawn.
 * @param branding The app's logo and name, drawn above the items. Null, the default, draws no
 * header at all.
 * @param pinStandardRoutes Whether [pinnedRoutes] drop to the bottom of the drawer once [items]
 * carries anything outside them. Defaults to true.
 * @param pinnedRoutes The routes that drop to the bottom while [pinStandardRoutes] is on. Defaults
 * to [NavigationDrawerRoutes.StandardRoutes].
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
    pinStandardRoutes: Boolean = true,
    pinnedRoutes: ImmutableSet<String> = NavigationDrawerRoutes.StandardRoutes,
    isSelected: (NavigationDrawerItem) -> Boolean = { item -> item.route == currentRoute },
    onItemClick: (NavigationDrawerItem) -> Unit = {},
    headerContent: (@Composable ColumnScope.() -> Unit)? = {
        Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
    },
) {
    val plan: NavigationDrawerPlan = remember(items, pinnedRoutes, pinStandardRoutes) {
        navigationDrawerPlan(
            items = items,
            pinnedRoutes = pinnedRoutes,
            pinStandardRoutes = pinStandardRoutes,
        )
    }

    ModalDrawerSheet(
        modifier = modifier,
        drawerState = drawerState,
    ) {
        headerContent?.invoke(this)

        if (branding != null) {
            NavigationDrawerHeader(branding = branding)
            Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
        }

        plan.leading.forEach { item ->
            DrawerItem(
                item = item,
                isSelected = isSelected,
                dividerRoutes = dividerRoutes,
                onItemClick = onItemClick,
            )
        }

        if (plan.pinFooterToBottom) {
            Spacer(modifier = Modifier.weight(weight = 1f))
        }

        plan.footer.forEach { item ->
            DrawerItem(
                item = item,
                isSelected = isSelected,
                dividerRoutes = dividerRoutes,
                onItemClick = onItemClick,
            )
        }
    }
}

/**
 * How [NavigationDrawerSheet] lays its items out: everything in [leading], then the weighted spacer
 * when [pinFooterToBottom], then [footer].
 *
 * A drawer that pins nothing puts every item in [footer] and leaves [pinFooterToBottom] off, so the
 * list renders top aligned in the order it was given.
 */
@Immutable
internal data class NavigationDrawerPlan(
    val leading: List<NavigationDrawerItem>,
    val footer: List<NavigationDrawerItem>,
    val pinFooterToBottom: Boolean,
)

/**
 * Decides which items sit above the drawer's spacer and which drop to its bottom edge.
 *
 * Pinning only does something once the host adds a destination outside [pinnedRoutes]. A drawer
 * holding nothing but the standard entries has nothing to separate them from, so it renders as one
 * top aligned block whether or not [pinStandardRoutes] is on.
 */
internal fun navigationDrawerPlan(
    items: List<NavigationDrawerItem>,
    pinnedRoutes: Set<String>,
    pinStandardRoutes: Boolean,
): NavigationDrawerPlan {
    if (!pinStandardRoutes) {
        return NavigationDrawerPlan(
            leading = emptyList(),
            footer = items,
            pinFooterToBottom = false,
        )
    }

    val leading: List<NavigationDrawerItem> = items.filterNot { item -> item.route in pinnedRoutes }
    if (leading.isEmpty()) {
        return NavigationDrawerPlan(
            leading = emptyList(),
            footer = items,
            pinFooterToBottom = false,
        )
    }

    return NavigationDrawerPlan(
        leading = leading,
        footer = items.filter { item -> item.route in pinnedRoutes },
        pinFooterToBottom = true,
    )
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
