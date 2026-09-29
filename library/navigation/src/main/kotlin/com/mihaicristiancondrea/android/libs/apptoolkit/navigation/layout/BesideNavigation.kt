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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator

/**
 * Set by the shell while its pages open beside a rail or a permanent drawer.
 *
 * The page the navigation opened stands in for a tab there, so it is drawn like one: no back
 * button (the navigation beside it is the way out), the tab's small app bar, and, when the
 * navigation and the app bar share a colour, the same frame colour behind the bar and the content
 * in a card.
 *
 * @param tinted Whether the navigation and the app bar share [frameColor]; the content then sits
 * in a card of [ContentCardShape].
 * @param frameColor The colour behind the navigation right now, which may be animating.
 */
@Stable
class BesideNavigation(
    val tinted: Boolean,
    val frameColor: () -> Color,
)

/** The shell's pages open beside the navigation; null when they cover the window. */
val LocalBesideNavigation = compositionLocalOf<BesideNavigation?> { null }

/** The corner the content card takes beside a tinted rail or drawer, under the app bar. */
val ContentCardShape: Shape = RoundedCornerShape(topStart = 24.dp)

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
    // Plain memory rather than state: it only ever changes along with the stack it is read from.
    val last = remember(key) { TopLevelMemory() }
    val index = pages.indexOf(key)
    if (index >= 0) last.value = index == 1
    return last.value
}

private class TopLevelMemory(var value: Boolean = false)
