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

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * One row of the navigation drawer, or of the rail once it holds more than the tabs, or of the
 * app bar's overflow menu.
 *
 * The drawer lists what the tabs do not: destinations opened as pages and one-off actions such as
 * sharing the app. A [Spacer] splits the list in two and pins everything after it to the bottom
 * edge, the way the drawers of Google's apps keep Settings and Help at the foot.
 */
@Immutable
sealed interface DrawerEntry {
    @Immutable
    class Link(
        val key: NavKey,
        @param:StringRes val label: Int,
        val icon: ToolkitIcon,
        @param:StringRes override val shortLabel: Int? = null,
    ) : DrawerEntry

    /** Runs [onClick] instead of navigating, for entries such as Share that leave the app. */
    @Immutable
    class Action(
        @param:StringRes val label: Int,
        val icon: ToolkitIcon,
        val onClick: (Context) -> Unit,
        @param:StringRes override val shortLabel: Int? = null,
    ) : DrawerEntry

    data object Spacer : DrawerEntry {
        override val shortLabel: Int? = null
    }

    /** A shorter label for the collapsed rail, where the full one would be cut off. */
    @get:StringRes
    val shortLabel: Int?
}
