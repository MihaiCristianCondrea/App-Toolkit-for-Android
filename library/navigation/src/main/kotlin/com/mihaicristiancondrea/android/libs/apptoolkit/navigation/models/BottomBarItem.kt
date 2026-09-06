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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * Represents an item rendered in bottom navigation surfaces (bottom bar and navigation rail).
 *
 * @property route Stable route key used for navigation.
 * @property icon Icon displayed when the destination is not selected.
 * @property selectedIcon Icon displayed when the destination is selected.
 * @property title String resource id used for both label and content description.
 * @property badgeText Optional badge text. When blank, no badge is shown.
 */
@Immutable
data class BottomBarItem<T : StableNavKey>(
    val route: T,
    val icon: ToolkitIcon,
    val selectedIcon: ToolkitIcon = icon,
    val title: Int,
    val badgeText: String = "",
) {
    /**
     * Secondary constructor for Compose [ImageVector] icons.
     */
    constructor(
        route: T,
        icon: ImageVector,
        selectedIcon: ImageVector,
        title: Int,
        badgeText: String = "",
    ) : this(
        route = route,
        icon = ToolkitIcon.Vector(icon),
        selectedIcon = ToolkitIcon.Vector(selectedIcon),
        title = title,
        badgeText = badgeText,
    )

    /**
     * Secondary constructor for drawable or vector resource ID icons.
     */
    constructor(
        route: T,
        @DrawableRes iconResId: Int,
        @DrawableRes selectedIconResId: Int = iconResId,
        title: Int,
        badgeText: String = "",
    ) : this(
        route = route,
        icon = ToolkitIcon.Resource(iconResId),
        selectedIcon = ToolkitIcon.Resource(selectedIconResId),
        title = title,
        badgeText = badgeText,
    )
}
