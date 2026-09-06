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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an item in a navigation drawer.
 *
 * @property title The resource ID of the string to display as the title of the item.
 * @property icon The icon to display when the item is unselected.
 * @property selectedIcon The icon to display when the item is selected, defaults to [icon].
 * @property route The unique identifier used for handling navigation actions.
 * @property badgeText An optional string to display as a badge on the item, defaults to an empty string.
 */
@Immutable
data class NavigationDrawerItem(
    val title: Int,
    val icon: NavigationIcon,
    val selectedIcon: NavigationIcon = icon,
    val route: String,
    val badgeText: String = "",
) {
    /**
     * Secondary constructor for Compose [ImageVector] icons.
     */
    constructor(
        title: Int,
        icon: ImageVector,
        selectedIcon: ImageVector = icon,
        route: String,
        badgeText: String = "",
    ) : this(
        title = title,
        icon = NavigationIcon.Vector(icon),
        selectedIcon = NavigationIcon.Vector(selectedIcon),
        route = route,
        badgeText = badgeText,
    )

    /**
     * Secondary constructor for drawable or vector resource ID icons.
     */
    constructor(
        title: Int,
        @DrawableRes iconResId: Int,
        @DrawableRes selectedIconResId: Int = iconResId,
        route: String,
        badgeText: String = "",
    ) : this(
        title = title,
        icon = NavigationIcon.Resource(iconResId),
        selectedIcon = NavigationIcon.Resource(selectedIconResId),
        route = route,
        badgeText = badgeText,
    )
}
