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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ContentWidth
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle

/**
 * How the shell presents its navigation.
 *
 * | Mode | Tabs | Drawer entries | Menu button |
 * |---|---|---|---|
 * | [BottomBar] | Navigation bar, unless the host hides its single tab bar | Modal drawer | In the app bar, opens the drawer |
 * | [Rail] | Collapsed rail | Same rail, scrollable | Top of the rail, opens the rail expanded over the content |
 * | [ExpandedRail] | Expanded rail beside the content | Same rail, scrollable | Top of the rail, collapses and expands it in place |
 * | [PermanentDrawer] | Always-open drawer | Same drawer | None |
 */
enum class ShellLayoutMode {
    /** Picks one of the others from the window width. See [ShellLayoutPolicy]. */
    Auto,
    BottomBar,
    Rail,
    ExpandedRail,
    PermanentDrawer,
}

/**
 * The window widths at which [ShellLayoutMode.Auto] moves to the next mode, and at which list and
 * detail pages start sharing the window, with an opt-in to hide a single tab's bottom bar.
 * The default widths are Material's window size class breakpoints: medium at 600dp, expanded
 * at 840dp and large at 1200dp.
 */
@Immutable
data class ShellLayoutPolicy(
    val railFrom: Dp = 600.dp,
    val expandedRailFrom: Dp = 840.dp,
    val permanentDrawerFrom: Dp = 1200.dp,
    val listDetailFrom: Dp = 600.dp,
    /**
     * The widest a destination's content grows before the shell centres it with margins on both
     * sides. [Dp.Unspecified] lets content fill any window. Destinations opt out with
     * [ContentWidth.Full].
     */
    val contentMaxWidth: Dp = Dp.Unspecified,
    /** Below this window height a large app bar would take too much of the screen; a small one is drawn. */
    val largeTopBarFrom: Dp = 480.dp,
    /**
     * Omits the bottom navigation bar when the graph has exactly one tab. The phone's menu
     * and modal drawer, and every wide navigation layout, remain available. A second tab
     * restores the bar automatically. Defaults to false to preserve existing hosts.
     */
    val hideSingleTabBottomBar: Boolean = false,
) {
    fun resolve(requested: ShellLayoutMode, width: Dp): ShellLayoutMode = when {
        requested != ShellLayoutMode.Auto -> requested
        width >= permanentDrawerFrom -> ShellLayoutMode.PermanentDrawer
        width >= expandedRailFrom -> ShellLayoutMode.ExpandedRail
        width >= railFrom -> ShellLayoutMode.Rail
        else -> ShellLayoutMode.BottomBar
    }

    /** Whether [resolve] gives [ShellLayoutMode.BottomBar] for some window: every threshold is above zero. */
    val reachesBottomBar: Boolean
        get() = widthThresholds.none { 0.dp >= it }

    /**
     * Whether [resolve] gives a rail or the permanent drawer for some window: one threshold is a
     * finite width. [Dp.Infinity] turns a mode off.
     */
    val reachesWideNavigation: Boolean
        get() = widthThresholds.any { it.value.isFinite() }

    private val widthThresholds: List<Dp>
        get() = listOf(railFrom, expandedRailFrom, permanentDrawerFrom)
}

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

@Composable
fun rememberShellLayoutInfo(
    policy: ShellLayoutPolicy,
    requested: ShellLayoutMode,
    limitContentWidth: Boolean,
): ShellLayoutInfo {
    val size = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val width = with(density) { size.width.toDp() }
    val height = with(density) { size.height.toDp() }
    return ShellLayoutInfo(
        mode = policy.resolve(requested, width),
        windowWidth = width,
        windowHeight = height,
        listDetail = width >= policy.listDetailFrom,
        contentMaxWidth = if (limitContentWidth) policy.contentMaxWidth else Dp.Unspecified,
        allowsLargeTopBar = height >= policy.largeTopBarFrom,
    )
}

val LocalShellLayout = staticCompositionLocalOf {
    ShellLayoutInfo(ShellLayoutMode.BottomBar, 0.dp, 0.dp, listDetail = false)
}
