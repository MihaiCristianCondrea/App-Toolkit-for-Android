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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph

/**
 * The tab to draw as selected, or [NoTab]. A tab reads as selected while its root is on screen: not
 * while a child covers it, but still when the child is the detail open beside it. With
 * [pagesReplaceTab], as beside a rail or drawer where an open page takes the tab's place, not
 * while a page is open either; a bottom bar under a page keeps its tab marked for when it returns.
 */
internal fun highlightedTab(
    graph: ShellGraph,
    navigator: ShellNavigator,
    listDetail: Boolean,
    pagesReplaceTab: Boolean = false,
): Int {
    // A graph of pages only has no tab to mark.
    if (graph.tabs.isEmpty() || (pagesReplaceTab && navigator.pages.size > 1)) return NoTab
    val tabStack = navigator.currentTabStack
    val rootVisible = tabStack.size == 1 || (
        listDetail && tabStack.size == 2 &&
            graph.destination(tabStack[0]).paneRole == PaneRole.List &&
            graph.destination(tabStack[1]).paneRole == PaneRole.Detail
        )
    return if (rootVisible) navigator.currentTabIndex else NoTab
}

/** No tab is shown as selected. */
internal const val NoTab = -1
