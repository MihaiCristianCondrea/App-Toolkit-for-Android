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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * A root of the shell, as it appears in the navigation bar, the rail and the permanent drawer.
 *
 * [icon] and [selectedIcon] take any [ToolkitIcon]: an animated vector drawable or a Lottie icon
 * plays when the tab is clicked or becomes selected, as in the Toolkit's other navigation items.
 * Pass the same animated icon for both to play one animation for both states.
 *
 * A tab shows [badge], a fixed label, or [badgeText], read in composition so it follows state such
 * as an unread count. Either one returning null shows no badge, and an empty string shows a dot.
 */
@Immutable
class ShellTab(
    val key: NavKey,
    @param:StringRes val label: Int,
    val icon: ToolkitIcon,
    val selectedIcon: ToolkitIcon = icon,
    val badge: String? = null,
    val badgeText: (@Composable () -> String?)? = null,
    /** When set, the tab's app bar holds a search field in place of the title. */
    val search: TabSearch? = null,
    /**
     * A shorter label for the places with little room under an icon: the navigation bar and the
     * collapsed rail. [label] is used everywhere else, and there too when this is null.
     */
    @param:StringRes val shortLabel: Int? = null,
) {
    init {
        require(badge == null || badgeText == null) { "A tab takes a badge or a badgeText, not both." }
    }
}
