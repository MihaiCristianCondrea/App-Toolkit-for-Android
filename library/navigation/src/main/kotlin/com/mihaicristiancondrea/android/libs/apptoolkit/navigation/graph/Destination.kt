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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import kotlin.reflect.KClass

/**
 * Where a destination is shown, and therefore how it enters, leaves and answers the back gesture.
 *
 * The shell has two levels. The outer level holds the shell itself and every [Page], and moves
 * between them the way Android moves between activities. The inner level lives inside the shell,
 * under its app bar and above its navigation bar or rail, and holds the [Tab] roots and their
 * [Child] destinations.
 */
enum class DestinationKind {
    /** A root of the shell, selected from the bottom bar, the rail or the drawer. */
    Tab,

    /**
     * Pushed on top of the current tab, inside the shell. The navigation bar stays; the app bar
     * shows a back arrow and the child's title.
     */
    Child,

    /**
     * Covers the whole shell, bars included, and leaves with the system back animation, the way a
     * separate activity would. Settings, help and anything opened from the drawer are pages.
     */
    Page,
}

/**
 * The part a page plays when the window is wide enough to show two pages side by side.
 *
 * A [List] page shows beside the [Detail] page opened from it. Below that width both are plain
 * pages and the detail covers the list.
 */
enum class PaneRole { None, List, Detail }

/** Everything the shell needs to know to show one kind of [NavKey]. */
@Immutable
class Destination<K : NavKey> @PublishedApi internal constructor(
    val keyClass: KClass<K>,
    val kind: DestinationKind,
    val topBar: TopBarStyle,
    val paneRole: PaneRole,
    val contentWidth: ContentWidth,
    /**
     * Whether the shell wraps [content] in a page frame titled [title]. Only pages use it; tabs
     * and children are always drawn under the shell's own app bar.
     */
    val scaffold: Boolean,
    val title: (@Composable (K) -> String)?,
    val actions: (@Composable RowScope.(K) -> Unit)?,
    val content: @Composable (K) -> Unit,
    /** How this destination enters and leaves; null takes its kind's default from [ShellGraph.transitions]. */
    val transition: ScreenTransition? = null,
    /**
     * A floating action button shown while this destination is on top: above the navigation bar,
     * the banner and the mini player on a tab or child, in the page's frame on a page.
     */
    val floatingActionButton: (@Composable (K) -> Unit)? = null,
)

/**
 * A root of the shell, as it appears in the navigation bar, the rail and the permanent drawer.
 *
 * [icon] and [selectedIcon] take any [ToolkitIcon]: an animated vector drawable or a Lottie icon
 * plays when the tab is clicked or becomes selected, as in the Toolkit's other navigation items.
 * Pass the same animated icon for both to play one animation for both states.
 */
@Immutable
class ShellTab(
    val key: NavKey,
    @param:StringRes val label: Int,
    val icon: ToolkitIcon,
    val selectedIcon: ToolkitIcon = icon,
    val badge: String? = null,
    /** When set, the tab's app bar holds a search field in place of the title. */
    val search: TabSearch? = null,
    /**
     * A shorter label for the places with little room under an icon: the navigation bar and the
     * collapsed rail. [label] is used everywhere else, and there too when this is null.
     */
    @param:StringRes val shortLabel: Int? = null,
)

/**
 * A search field in a tab's app bar, where the title would be.
 *
 * The field is part of the shell's app bar, so moving to the tab only crossfades the title into
 * the field: the navigation button, the bar and its height stay exactly where they were. The
 * query lives in the shell; the tab reads it from
 * [com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch]. Back clears a
 * query before it does anything else.
 */
@Immutable
class TabSearch(@param:StringRes val hint: Int)

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
