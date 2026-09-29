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
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellHomeRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellTransitions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import kotlin.reflect.KClass

import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR

/**
 * The complete description of an app: its tabs, drawer, pages and bottom accessories.
 *
 * Features add their pages with registration functions on [ShellGraphBuilder]; the host builds the
 * graph once and hands it to the shell.
 */
@Immutable
class ShellGraph internal constructor(
    @param:StringRes val appTitle: Int,
    /** The app's mark, shown beside [appTitle] where the navigation names the app. */
    val appIcon: ToolkitIcon?,
    val tabs: List<ShellTab>,
    val drawer: List<DrawerEntry>,
    /** The app bar's overflow menu on the tabs' roots. Empty draws no menu button. */
    val overflow: List<DrawerEntry>,
    val banner: (@Composable () -> Unit)?,
    val player: ShellPlayer?,
    /** The default transition of each kind of destination, and per navigation layout. */
    val transitions: ShellTransitions,
    /**
     * Where the app opens: a tab, which shows the shell on it, or a page, shown on its own before
     * the shell as a start screen. The first tab unless the app says otherwise.
     */
    val start: NavKey,
    /** The start screens a start screen choice offers besides the tabs. */
    val startScreens: List<NavKey>,
    /** Turns intents the app receives into destinations; see [ShellGraphBuilder.deepLinks]. */
    private val deepLinks: List<(Intent) -> NavKey?>,
    private val destinations: Map<KClass<out NavKey>, Destination<*>>,
) {
    init {
        require(tabs.isNotEmpty()) { "A shell needs at least one tab." }
        tabs.forEach { tab ->
            require(destinationOrNull(tab.key)?.kind == DestinationKind.Tab) {
                "${tab.key} is listed as a tab but was not registered with tab()."
            }
        }
        (startScreens + start).forEach { key ->
            val kind = destinationOrNull(key)?.kind
            require(kind == DestinationKind.Tab || kind == DestinationKind.Page) {
                "$key cannot be where the app starts: register it with tab() or page()."
            }
        }
    }

    /**
     * The destination [intent] asks for, from the first deep link that recognises it, or null
     * when none does or the destination is not registered.
     */
    fun keyFor(intent: Intent): NavKey? =
        deepLinks.firstNotNullOfOrNull { it(intent) }?.takeIf { it == ShellHomeRoute || contains(it) }

    /** Every place the app can start: the tabs, then the start screens. */
    val startOptions: List<NavKey> get() = tabs.map { it.key } + startScreens

    val startTab: NavKey get() = tabs.first().key

    fun tabIndexOf(key: NavKey): Int = tabs.indexOfFirst { it.key == key }

    /** Whether [key] can be navigated to. */
    fun contains(key: NavKey): Boolean = destinationOrNull(key) != null

    @Suppress("UNCHECKED_CAST")
    fun destination(key: NavKey): Destination<NavKey> =
        checkNotNull(destinationOrNull(key)) {
            "No destination is registered for ${key::class.qualifiedName}. Register it in the shell graph."
        } as Destination<NavKey>

    /** The transition [key] enters and leaves with in [layout]. */
    fun transitionOf(key: NavKey, layout: ShellLayoutMode): ScreenTransition {
        val destination = destinationOrNull(key) ?: return transitions.pages
        return transitions.resolve(destination.kind, destination.transition, layout)
    }

    private fun destinationOrNull(key: NavKey): Destination<*>? =
        if (key == ShellHomeRoute) null else destinations[key::class]
}

/**
 * Collects an app's destinations. The host creates it, lets each feature and the app register
 * their destinations, and calls [build].
 */
class ShellGraphBuilder(
    @param:StringRes private val appTitle: Int,
    private val appIcon: ToolkitIcon? = null,
) {

    @PublishedApi
    internal val destinations: MutableMap<KClass<out NavKey>, Destination<*>> = linkedMapOf()

    @PublishedApi
    internal val tabs: MutableList<ShellTab> = mutableListOf()

    private val drawerEntries: MutableList<DrawerEntry> = mutableListOf()
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
        topBar: TopBarStyle = TopBarStyle.Small,
        contentWidth: ContentWidth = ContentWidth.Default,
        badge: String? = null,
        search: TabSearch? = null,
        @StringRes shortLabel: Int? = null,
        paneRole: PaneRole = PaneRole.None,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline content: @Composable (K) -> Unit,
    ) {
        tabs += ShellTab(key = key, label = label, icon = icon, selectedIcon = selectedIcon, badge = badge, search = search, shortLabel = shortLabel)
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Tab,
                topBar = topBar,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = false,
                title = null,
                actions = actions,
                content = content,
                floatingActionButton = fab,
            ),
        )
    }

    /** Adds a destination pushed inside the current tab, keeping the navigation bar. */
    inline fun <reified K : NavKey> child(
        topBar: TopBarStyle = TopBarStyle.Small,
        paneRole: PaneRole = PaneRole.None,
        contentWidth: ContentWidth = ContentWidth.Default,
        noinline title: @Composable (K) -> String,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        transition: ScreenTransition? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline content: @Composable (K) -> Unit,
    ) {
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Child,
                topBar = topBar,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = false,
                title = title,
                actions = actions,
                content = content,
                floatingActionButton = fab,
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
     */
    inline fun <reified K : NavKey> page(
        topBar: TopBarStyle = TopBarStyle.Large,
        paneRole: PaneRole = PaneRole.None,
        contentWidth: ContentWidth = ContentWidth.Default,
        noinline title: (@Composable (K) -> String)? = null,
        noinline actions: (@Composable RowScope.(K) -> Unit)? = null,
        transition: ScreenTransition? = null,
        noinline fab: (@Composable (K) -> Unit)? = null,
        noinline content: @Composable (K) -> Unit,
    ) {
        register(
            Destination(
                keyClass = K::class,
                kind = DestinationKind.Page,
                topBar = topBar,
                paneRole = paneRole,
                contentWidth = contentWidth,
                scaffold = title != null,
                title = title,
                actions = actions,
                content = content,
                floatingActionButton = fab,
                transition = transition,
            ),
        )
    }

    /**
     * Adds a page unless the app registered its own for [K]. Toolkit features register their
     * pages with it, so an app replaces any of them by registering the same key first.
     */
    inline fun <reified K : NavKey> pageIfAbsent(
        paneRole: PaneRole,
        noinline title: @Composable (K) -> String,
        topBar: TopBarStyle = TopBarStyle.Large,
        noinline content: @Composable (K) -> Unit,
    ) {
        if (K::class !in destinations) page(topBar = topBar, paneRole = paneRole, title = title, content = content)
    }

    /** Lists the drawer's entries, top to bottom. */
    fun drawer(builder: DrawerBuilder.() -> Unit) {
        drawerEntries += DrawerBuilder().apply(builder).entries
    }

    /**
     * Lists the entries of the app bar's overflow menu, shown on the tabs' roots. A [DrawerEntry.Spacer]
     * draws a divider. `supportUs()` adds the Support page, the way the Toolkit's app bar offers it.
     */
    fun overflow(builder: DrawerBuilder.() -> Unit) {
        overflowEntries += DrawerBuilder().apply(builder).entries
    }

    /** Shown above the navigation bar, such as an ad. Steps aside while the player is showing. */
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
     * a launcher shortcut, a notification, a link, or the system asking for a screen, such as
     * `Intent.ACTION_VIEW_PERMISSION_USAGE`. The host opens the destination as if a screen had
     * called `navigate(key)`.
     *
     * ```
     * deepLinks {
     *     action("com.example.action.OPEN_SETTINGS") { SettingsRoute }
     *     match { intent -> intent.data?.lastPathSegment?.toIntOrNull()?.let(::ItemRoute) }
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
        drawer = drawerEntries.toList(),
        overflow = overflowEntries.toList(),
        banner = banner,
        player = player,
        transitions = transitions,
        start = start ?: tabs.first().key,
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

/** Collects [ShellGraphBuilder.deepLinks]. The first link that returns a key wins. */
class DeepLinkBuilder internal constructor() {
    internal val links = mutableListOf<(Intent) -> NavKey?>()

    /** Intents with [action] open the destination [key] returns for them. */
    fun action(action: String, key: (Intent) -> NavKey?) {
        links += { intent -> if (intent.action == action) key(intent) else null }
    }

    /** Any intent [key] recognises, by its data, extras or anything else. */
    fun match(key: (Intent) -> NavKey?) {
        links += key
    }
}

class DrawerBuilder internal constructor() {
    internal val entries = mutableListOf<DrawerEntry>()

    /** An entry that opens [key], normally a page. */
    fun link(key: NavKey, @StringRes label: Int, icon: ToolkitIcon, @StringRes shortLabel: Int? = null) {
        entries += DrawerEntry.Link(key, label, icon, shortLabel)
    }

    /** An entry that runs [onClick] instead of navigating. */
    fun action(
        @StringRes label: Int,
        icon: ToolkitIcon,
        @StringRes shortLabel: Int? = null,
        onClick: (Context) -> Unit,
    ) {
        entries += DrawerEntry.Action(label, icon, onClick, shortLabel)
    }

    /** The Support page: rating, sharing and donations. The support feature registers it. */
    fun supportUs() {
        link(
            key = SupportRoute,
            label = R.string.shell_support_us,
            icon = ToolkitIcon.Vector(Icons.Outlined.VolunteerActivism),
            shortLabel = R.string.shell_support_short,
        )
    }

    /** Pins every entry after it to the bottom of the drawer. */
    fun spacer() {
        entries += DrawerEntry.Spacer
    }

    /**
     * The settings list, with the Toolkit's standard label and animated icon, the same as
     * `DefaultNavigationRepository`'s entry. The settings feature registers the page.
     */
    fun settings() {
        link(SettingsRoute, R.string.settings, ToolkitIcon.AnimatedVector(DesignSystemR.drawable.anim_settings))
    }
}
