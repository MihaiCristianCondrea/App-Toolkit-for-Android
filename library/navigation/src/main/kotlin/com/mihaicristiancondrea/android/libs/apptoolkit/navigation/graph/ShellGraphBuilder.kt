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

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellTransitions
import kotlin.reflect.KClass

/**
 * Collects an app's destinations. The host creates it, lets each feature and the app register
 * their destinations, and calls [build].
 *
 * A destination registered without a `topBar` takes the style [defaults] gives its kind.
 */
class ShellGraphBuilder(
    @param:StringRes private val appTitle: Int,
    private val appIcon: ToolkitIcon? = null,
    val defaults: NavigationDefaults = NavigationDefaults(),
) {

    @PublishedApi
    internal val destinations: MutableMap<KClass<out NavKey>, Destination<*>> = linkedMapOf()

    @PublishedApi
    internal val tabs: MutableList<ShellTab> = mutableListOf()

    private val drawerEntries: MutableList<DrawerEntry> = mutableListOf()
    private val drawerFooter: MutableList<DrawerEntry> = mutableListOf()
    private val overflowEntries: MutableList<DrawerEntry> = mutableListOf()
    private var banner: (@Composable () -> Unit)? = null
    private var player: ShellPlayer? = null
    private var transitions: ShellTransitions = ShellTransitions()
    private var start: NavKey? = null
    private val startScreens: MutableList<NavKey> = mutableListOf()
    private val deepLinks: MutableList<(Intent) -> NavKey?> = mutableListOf()

    /**
     * Adds a root of the shell. Tabs appear in the order they are added, and the first is where
     * the app starts and where back ends before the app closes.
     *
     * A tab with [paneRole] `List` shows a child with `PaneRole.Detail` beside it on wide windows,
     * with a draggable separator, instead of pushing it over the tab.
     */
    inline fun <reified K : NavKey> tab(
        key: K,
        @StringRes label: Int,
        icon: ToolkitIcon,
        selectedIcon: ToolkitIcon = icon,
        topBar: TopBarStyle? = null,
        contentWidth: ContentWidth = ContentWidth.Default,
        badge: String? = null,
        noinline badgeText: (@Composable () -> String?)? = null,
        search: TabSearch? = null,
        @StringRes shortLabel: Int? = null,
        paneRole: PaneRole = PaneRole.None,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline fabs: (@Composable (K) -> List<ToolkitFab>)? = null,
        topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
        noinline content: @Composable (K) -> Unit,
    ) {
        tabs += ShellTab(
            key = key,
            label = label,
            icon = icon,
            selectedIcon = selectedIcon,
            badge = badge,
            badgeText = badgeText,
            search = search,
            shortLabel = shortLabel,
        )
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Tab,
                topBar = topBar ?: defaults.tabTopBarStyle,
                topBarScrollMode = topBarScrollMode,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = false,
                title = null,
                actions = actions,
                content = content,
                floatingActionButton = fab,
                floatingActionButtons = fabs,
            ),
        )
    }

    /** Adds a destination pushed inside the current tab, keeping the navigation bar. */
    inline fun <reified K : NavKey> child(
        topBar: TopBarStyle? = null,
        paneRole: PaneRole = PaneRole.None,
        contentWidth: ContentWidth = ContentWidth.Default,
        noinline title: @Composable (K) -> String,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        transition: ScreenTransition? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline fabs: (@Composable (K) -> List<ToolkitFab>)? = null,
        topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
        noinline content: @Composable (K) -> Unit,
    ) {
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Child,
                topBar = topBar ?: defaults.childTopBarStyle,
                topBarScrollMode = topBarScrollMode,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = false,
                title = title,
                actions = actions,
                content = content,
                floatingActionButton = fab,
                floatingActionButtons = fabs,
                transition = transition,
            ),
        )
    }

    /**
     * Adds a destination that covers the shell like an activity would.
     *
     * With a [title] the shell draws the page's app bar and back button, and [content] only fills
     * the body. Without one, [content] owns the whole page and draws its own frame, for pages that
     * need tabs or a search field under their app bar.
     *
     * [backButton] false leaves the arrow out of that app bar, for a task the person finishes with
     * a button of its own; system back still closes the page unless [content] handles it.
     */
    inline fun <reified K : NavKey> page(
        topBar: TopBarStyle? = null,
        paneRole: PaneRole = PaneRole.None,
        contentWidth: ContentWidth = ContentWidth.Default,
        noinline title: (@Composable (K) -> String)? = null,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        transition: ScreenTransition? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline fabs: (@Composable (K) -> List<ToolkitFab>)? = null,
        noinline placeholder: (@Composable () -> Unit)? = null,
        topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
        backButton: Boolean = true,
        noinline content: @Composable (K) -> Unit,
    ) {
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Page,
                topBar = topBar ?: defaults.pageTopBarStyle,
                topBarScrollMode = topBarScrollMode,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = title != null,
                title = title,
                actions = actions,
                content = content,
                floatingActionButton = fab,
                floatingActionButtons = fabs,
                placeholder = placeholder,
                backButton = backButton,
                transition = transition,
            ),
        )
    }

    /**
     * Adds a page unless the app registered its own for [K]. Toolkit features register their
     * pages with it, so an app replaces any of them by registering the same key first.
     *
     * A null [title], as for a start screen, leaves the whole page to [content], as with [page].
     */
    inline fun <reified K : NavKey> pageIfAbsent(
        paneRole: PaneRole,
        noinline title: (@Composable (K) -> String)?,
        topBar: TopBarStyle? = null,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        noinline fabs: (@Composable (K) -> List<ToolkitFab>)? = null,
        noinline placeholder: (@Composable () -> Unit)? = null,
        topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
        noinline content: @Composable (K) -> Unit,
    ) {
        if (K::class !in destinations) {
            page(
                topBar = topBar,
                topBarScrollMode = topBarScrollMode,
                paneRole = paneRole,
                title = title,
                actions = actions,
                fabs = fabs,
                placeholder = placeholder,
                content = content,
            )
        }
    }

    /**
     * Lists the drawer's entries, top to bottom. Entries given to [DrawerBuilder.footer] come after
     * all of them, pinned to the bottom edge, whatever order the calls were made in.
     */
    fun drawer(builder: DrawerBuilder.() -> Unit) {
        val drawer = DrawerBuilder().apply(builder)
        drawerEntries += drawer.entries
        drawerFooter += drawer.footer
    }

    /**
     * Lists the entries of the app bar's overflow menu, shown on the tabs' roots. A [DrawerEntry.Spacer]
     * draws a divider. `supportUs()` adds the Support page, the way the Toolkit's app bar offers it.
     */
    fun overflow(builder: DrawerBuilder.() -> Unit) {
        val overflow = DrawerBuilder().apply(builder)
        overflowEntries += overflow.entries + overflow.footer
    }

    /**
     * Optional host content above the navigation bar. Absent unless explicitly registered, and
     * steps aside while the player is showing.
     *
     * Warning: placing native ads next to navigation can cause accidental clicks. Prefer placing
     * them within scrolling content and review
     * [Google's placement guidance](https://support.google.com/admob/answer/10094971) before opting in.
     */
    fun banner(content: @Composable () -> Unit) {
        banner = content
    }

    fun player(player: ShellPlayer) {
        this.player = player
    }

    /**
     * Where the app opens, the first tab if this is not called. A tab opens the shell on it; a page
     * is shown on its own, with no shell under it, as a start screen: it enters the shell by
     * navigating to a tab or calling `ShellNavigator.enterShell()`, and back from it leaves the app.
     * The host can decide at launch instead, for a screen shown only once.
     */
    fun start(key: NavKey) {
        start = key
    }

    /**
     * Maps the intents the app's activity receives, at launch or while running, to destinations:
     * a launcher shortcut, a notification, a widget or a link. The host opens the destination as
     * if a screen had called `navigate(key)`.
     *
     * ```
     * deepLinks {
     *     action("com.example.action.OPEN_SETTINGS") { SettingsScreen }
     *     match { intent -> intent.data?.lastPathSegment?.toIntOrNull()?.let(::ItemScreen) }
     * }
     * ```
     */
    fun deepLinks(builder: DeepLinkBuilder.() -> Unit) {
        deepLinks += DeepLinkBuilder().apply(builder).links
    }

    /**
     * Pages a start screen choice offers besides the tabs, to try a start screen without making it
     * the app's start.
     */
    fun startScreens(vararg keys: NavKey) {
        startScreens += keys
    }

    /**
     * The transition each kind of destination uses when it declares none: children slide, pages
     * move like activities, unless changed here, for every layout or per layout.
     */
    fun transitions(value: ShellTransitions) {
        transitions = value
    }

    fun build(): ShellGraph = ShellGraph(
        appTitle = appTitle,
        appIcon = appIcon,
        tabs = tabs.toList(),
        drawer = drawerEntries + when {
            drawerFooter.isEmpty() -> emptyList()
            // Already split: whatever follows the spacer is at the bottom, and the footer after it.
            DrawerEntry.Spacer in drawerEntries -> drawerFooter
            else -> listOf(DrawerEntry.Spacer) + drawerFooter
        },
        overflow = overflowEntries.toList(),
        banner = banner,
        player = player,
        transitions = transitions,
        defaults = defaults,
        start = start ?: requireNotNull(tabs.firstOrNull()?.key) {
            "A graph without tabs needs start(page)."
        },
        startScreens = startScreens.toList(),
        deepLinks = deepLinks.toList(),
        destinations = destinations.toMap(),
    )

    @PublishedApi
    internal fun register(destination: Destination<*>) {
        check(destinations.put(destination.keyClass, destination) == null) {
            "${destination.keyClass.qualifiedName} is registered twice."
        }
    }
}
