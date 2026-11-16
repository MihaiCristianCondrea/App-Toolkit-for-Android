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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ContentWidth
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle

/** The layout the shell is currently drawing, and the window it is drawing it for. */
@Immutable
data class ShellLayoutInfo(
    /** Never [ShellLayoutMode.Auto]. */
    val mode: ShellLayoutMode,
    val windowWidth: Dp,
    val windowHeight: Dp,
    val listDetail: Boolean,
    /** [ShellLayoutPolicy.contentMaxWidth], or [Dp.Unspecified] when the developer options lift it. */
    val contentMaxWidth: Dp = Dp.Unspecified,
    /** Whether the window is tall enough for large, collapsing app bars. */
    val allowsLargeTopBar: Boolean = true,
) {
    /** The width a destination's content is limited to, given its own [ContentWidth]. */
    fun maxWidthFor(contentWidth: ContentWidth): Dp = when (contentWidth) {
        ContentWidth.Default -> contentMaxWidth
        ContentWidth.Full -> Dp.Unspecified
        is ContentWidth.Max -> contentWidth.width
    }

    /** [style], unless the window is too short for a large bar. */
    fun topBarFor(style: TopBarStyle): TopBarStyle =
        if (style.isLarge && !allowsLargeTopBar) TopBarStyle.Small else style
}
