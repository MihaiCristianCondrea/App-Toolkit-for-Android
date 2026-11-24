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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator

/**
 * Whether [key] is the page the navigation beside it opened, which stands in for a tab: the first
 * page over the shell while pages open beside a rail or permanent drawer. Pages opened from it
 * stack above and keep their back button.
 *
 * A page keeps the answer it had while it animates out: once the navigation swaps it for another
 * entry it is no longer on the stack, and without this it would grow a back button and a large
 * app bar for the length of its exit.
 */
@Composable
fun isTopLevelPage(key: Any?): Boolean {
    if (key == null || LocalBesideNavigation.current == null) return false
    val pages = LocalShellNavigator.current.pages
    // Plain memory rather than state, written once composition has applied.
    val last = remember(key) { TopLevelMemory() }
    val index = pages.indexOf(key)
    SideEffect { if (index >= 0) last.value = index == 1 }
    return if (index >= 0) index == 1 else last.value
}

private class TopLevelMemory(var value: Boolean = false)
