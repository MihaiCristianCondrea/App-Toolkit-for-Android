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
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

/**
 * Displays a navigation drawer sheet containing a list of [NavigationDrawerItem]s.
 *
 * @param items A list of [NavigationDrawerItem]s to render.
 * @param modifier The [Modifier] to be applied to the [ModalDrawerSheet].
 * @param drawerState State of the drawer sheet.
 * @param currentRoute The route of the currently displayed destination, used for selection if [isSelected] is not provided.
 * @param dividerRoutes A set of route strings after which a horizontal divider should be drawn.
 * @param isSelected A lambda to determine whether a given item is currently selected.
 * @param onItemClick A lambda to handle item click events.
 * @param headerContent Optional content placed at the top of the drawer sheet, defaults to a vertical spacer.
 */
@Composable
fun NavigationDrawerSheet(
    items: ImmutableList<NavigationDrawerItem>,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    currentRoute: String? = null,
    dividerRoutes: ImmutableSet<String> = persistentSetOf(),
    isSelected: (NavigationDrawerItem) -> Boolean = { item -> item.route == currentRoute },
    onItemClick: (NavigationDrawerItem) -> Unit = {},
    headerContent: (@Composable ColumnScope.() -> Unit)? = {
        Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
    },
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerState = drawerState,
    ) {
        headerContent?.invoke(this)
        items.forEach { item ->
            NavigationDrawerItemContent(
                item = item,
                selected = isSelected(item),
                dividerRoutes = dividerRoutes,
            ) {
                onItemClick(item)
            }
        }
    }
}
