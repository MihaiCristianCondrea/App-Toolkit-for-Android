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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellHomeRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ShellTransitions
import kotlin.reflect.KClass

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
    /** The app's defaults the graph was built with; `ShellHost` takes its layout policy from them. */
    val defaults: NavigationDefaults,
    /** Turns intents the app receives into destinations; see [ShellGraphBuilder.deepLinks]. */
    private val deepLinks: List<(Intent) -> NavKey?>,
    private val destinations: Map<KClass<out NavKey>, Destination<*>>,
) {
    init {
        /**
         * A graph with no tabs starts and exits from its page entry point without entering the
         * shell.
         */
        require(tabs.isNotEmpty() || destinationOrNull(start)?.kind == DestinationKind.Page) {
            "A shell needs at least one tab, or a page to start on."
        }
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

    /** The first tab, or null in a graph of pages only. */
    val startTab: NavKey? get() = tabs.firstOrNull()?.key

    fun tabIndexOf(key: NavKey): Int = tabs.indexOfFirst { it.key == key }

    /** Whether [key] can be navigated to. */
    fun contains(key: NavKey): Boolean = destinationOrNull(key) != null

    /** How many destinations the graph registers, tabs included. */
    internal val destinationCount: Int
        get() = destinations.size

    /** Whether some page is drawn in the shell's page frame, with its app bar. */
    internal val hasFramedPages: Boolean
        get() = destinations.values.any { it.kind == DestinationKind.Page && it.scaffold && it.title != null }

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
 * Builds a [ShellGraph]: an app's tabs, children, pages, drawer, overflow menu, start screens and
 * deep links.
 *
 * ```
 * val graph = shellGraph(appTitle = R.string.app_name) {
 *     tab(HomeRoute, R.string.home, ToolkitIcon.Vector(Icons.Outlined.Home)) { HomeScreen() }
 *     child<ItemRoute>(title = { it.name }) { ItemScreen(it) }
 *     drawer { settings(); spacer(); link(HelpRoute, R.string.help, ToolkitIcon.Vector(Icons.Outlined.Help)) }
 * }
 * ```
 *
 * `:library:apptoolkit`'s `toolkitGraph { }` wraps this and adds the Toolkit's own pages.
 */
fun shellGraph(
    @StringRes appTitle: Int,
    appIcon: ToolkitIcon? = null,
    defaults: NavigationDefaults = NavigationDefaults(),
    builder: ShellGraphBuilder.() -> Unit,
): ShellGraph = ShellGraphBuilder(appTitle, appIcon, defaults).apply(builder).build()
