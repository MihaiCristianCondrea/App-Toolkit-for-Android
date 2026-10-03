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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
 * @param scrollTint Set while the shared colour follows scrolling: where the app bars beside the
 * navigation say how far to take it. Null while the colour is fixed.
 * @param frameColor The colour behind the navigation right now, which may be animating. Last, so
 * it can be passed as a trailing lambda.
 */
@Stable
class BesideNavigation(
    val tinted: Boolean,
    val scrollTint: FrameScrollTint? = null,
    val frameColor: () -> Color,
)

/**
 * How far the frame's shared colour should go, from `0` to `1`, and whether to jump there, as it
 * does while a large app bar blends as it collapses, rather than ease there.
 */
@Immutable
data class FrameTint(val amount: Float, val snap: Boolean = false) {
    companion object {
        /** Content at rest under its app bar: the frame keeps the surface colour. */
        val None: FrameTint = FrameTint(amount = 0f)

        /** Content scrolled under its app bar: the frame takes the shared colour. */
        val Full: FrameTint = FrameTint(amount = 1f)
    }
}

/**
 * The app bars beside the navigation that follow scrolling, in the order they appeared: the one on
 * top decides the frame's colour. The shell's tabs sit underneath, so a page the navigation opens
 * takes over while it is shown and hands back to the tab when it leaves.
 *
 * Bars join with [FollowScrollWithFrameTint]; the shell reads [current].
 */
@Stable
class FrameScrollTint {
    private val drivers = mutableStateListOf<() -> FrameTint>()

    /** What the bar on top asks for, or [FrameTint.None] when none is shown. */
    val current: FrameTint
        get() = drivers.lastOrNull()?.invoke() ?: FrameTint.None

    internal fun add(driver: () -> FrameTint, underneath: Boolean) {
        if (underneath) drivers.add(0, driver) else drivers.add(driver)
    }

    internal fun remove(driver: () -> FrameTint) {
        drivers.remove(driver)
    }
}

/**
 * Lets this app bar drive the shared colour of the navigation beside it, while the person's
 * settings have it follow scrolling, for as long as the bar is composed. It does nothing anywhere
 * else.
 *
 * [tint] is read in a snapshot, so reading the bar's scroll state there keeps the colour in step.
 *
 * @param underneath Whether the bar sits under pages, as the shell's own tabs do: a page shown
 * over it decides the colour even when the tab's bar appears later.
 */
@Composable
fun FollowScrollWithFrameTint(underneath: Boolean = false, tint: () -> FrameTint) {
    val scrollTint = LocalBesideNavigation.current?.scrollTint ?: return
    val latest by rememberUpdatedState(tint)
    DisposableEffect(scrollTint, underneath) {
        val driver: () -> FrameTint = { latest() }
        scrollTint.add(driver, underneath)
        onDispose { scrollTint.remove(driver) }
    }
}

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
