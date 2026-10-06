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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.isSpecified
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy

/**
 * What the app's declared shell can do: facts read from its [ShellGraph] and [ShellLayoutPolicy]
 * alone, so a setting can tell whether it has anything to change in this app.
 *
 * They describe the app, not the window it is drawn in now, and not the developer options: an app
 * whose policy shows a bottom bar on phones [usesBottomNavigation] while a tablet draws a rail, or
 * while a forced layout does.
 *
 * Build it with [of]; inside the shell, read [LocalShellCapabilities].
 *
 * @property tabCount How many tabs the graph declares.
 * @property startOptionCount How many places the app can start: its tabs, then its start screens.
 * @property declaresBanner Whether the graph declares a bottom banner.
 * @property declaresPlayer Whether the graph declares a player.
 * @property hasFramedPages Whether some page is drawn in the shell's page frame, with its app bar.
 * @property hasBackNavigation Whether the app has more than one destination to move between.
 * @property layoutReachesBottomBar Whether the layout policy resolves to the bottom bar on some window.
 * @property layoutReachesWideNavigation Whether the layout policy resolves to a rail or permanent
 * drawer on some window.
 * @property hasContentWidthLimit Whether the layout policy limits how wide content grows.
 * @property hideSingleTabBottomBar Whether the host omits a single tab's bottom bar.
 */
@Immutable
data class ShellCapabilities(
    val tabCount: Int,
    val startOptionCount: Int,
    val declaresBanner: Boolean,
    val declaresPlayer: Boolean,
    val hasFramedPages: Boolean,
    val hasBackNavigation: Boolean,
    val layoutReachesBottomBar: Boolean,
    val layoutReachesWideNavigation: Boolean,
    val hasContentWidthLimit: Boolean,
    val hideSingleTabBottomBar: Boolean = false,
) {
    /** Whether the app navigates between tabs, so the shell draws navigation chrome. */
    val hasTabs: Boolean
        get() = tabCount > 0

    /** Whether there is more than one tab to move between. */
    val hasMultipleTabs: Boolean
        get() = tabCount > 1

    /** Whether the declared tabs need a bottom bar, including in a forced phone layout. */
    val hasBottomNavigationTabs: Boolean
        get() = hasTabs && (!hideSingleTabBottomBar || hasMultipleTabs)

    /** Whether the app shows its tabs in a bottom navigation bar on some window. */
    val usesBottomNavigation: Boolean
        get() = hasBottomNavigationTabs && layoutReachesBottomBar

    /** Whether the app shows its tabs in a rail or permanent drawer on some window. */
    val usesWideNavigation: Boolean
        get() = hasTabs && layoutReachesWideNavigation

    /** Whether some app bar is drawn by the shell: the tabs' own, or a framed page's. */
    val hasShellTopBars: Boolean
        get() = hasTabs || hasFramedPages

    /** Whether the banner can be shown: it is declared, and docks on the tabs' navigation bar. */
    val hasBanner: Boolean
        get() = hasBottomNavigationTabs && declaresBanner

    /** Whether the player can be shown: it is declared, and docks in the tabs' chrome. */
    val hasPlayer: Boolean
        get() = hasTabs && declaresPlayer

    /** Whether the app has a bottom accessory to show or hide. */
    val hasAccessories: Boolean
        get() = hasBanner || hasPlayer

    /** Whether the app can start in more than one place. */
    val hasMultipleStartOptions: Boolean
        get() = startOptionCount > 1

    companion object {
        /** The capabilities of [graph] drawn with [policy]. */
        fun of(graph: ShellGraph, policy: ShellLayoutPolicy = ShellLayoutPolicy()): ShellCapabilities = ShellCapabilities(
            tabCount = graph.tabs.size,
            startOptionCount = graph.startOptions.size,
            declaresBanner = graph.banner != null,
            declaresPlayer = graph.player != null,
            hasFramedPages = graph.hasFramedPages,
            hasBackNavigation = graph.destinationCount > 1,
            layoutReachesBottomBar = policy.reachesBottomBar,
            layoutReachesWideNavigation = policy.reachesWideNavigation,
            hasContentWidthLimit = policy.contentMaxWidth.isSpecified,
            hideSingleTabBottomBar = policy.hideSingleTabBottomBar,
        )
    }
}

/** The capabilities of the app `ShellHost` is drawing. */
val LocalShellCapabilities = staticCompositionLocalOf<ShellCapabilities> {
    error("ShellCapabilities is only available inside ShellHost.")
}
