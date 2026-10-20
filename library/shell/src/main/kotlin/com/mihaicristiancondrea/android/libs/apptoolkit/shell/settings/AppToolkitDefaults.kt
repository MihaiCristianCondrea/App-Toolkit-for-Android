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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.NavigationDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle

/**
 * One app's defaults for the Toolkit: how its shell behaves and how its destinations look when
 * nobody chose otherwise.
 *
 * Each shell setting resolves as the person's saved developer option, then [shell], then the
 * Toolkit's own default. A saved choice is kept even when it equals the current default, so a later
 * release that changes [shell] reaches only the people who never chose, and resetting the developer
 * options returns to [shell], not to the Toolkit's defaults.
 *
 * Give [navigation] to the graph and [shell] to the host:
 *
 * ```
 * val defaults = appToolkitDefaults {
 *     shell { backEdgeStyle = BackEdgeStyle.FollowFinger }
 *     navigation { pageTopBarStyle = TopBarStyle.LargeCollapsed }
 * }
 * val graph = toolkitGraph(appTitle = R.string.app_name, defaults = defaults.navigation) { ... }
 * ShellHost(graph = graph, defaults = defaults.shell)
 * ```
 */
@Immutable
data class AppToolkitDefaults(
    val shell: ShellSettings = ShellSettings(),
    val navigation: NavigationDefaults = NavigationDefaults(),
)

/** Builds [AppToolkitDefaults]. Anything left out keeps the Toolkit's default. */
fun appToolkitDefaults(builder: AppToolkitDefaultsBuilder.() -> Unit): AppToolkitDefaults =
    AppToolkitDefaultsBuilder().apply(builder).build()

@DslMarker
annotation class AppToolkitDefaultsDsl

@AppToolkitDefaultsDsl
class AppToolkitDefaultsBuilder internal constructor() {
    private var shell = ShellSettings()
    private var navigation = NavigationDefaults()

    fun shell(builder: ShellDefaultsBuilder.() -> Unit) {
        shell = ShellDefaultsBuilder(shell).apply(builder).build()
    }

    fun navigation(builder: NavigationDefaultsBuilder.() -> Unit) {
        navigation = NavigationDefaultsBuilder(navigation).apply(builder).build()
    }

    internal fun build(): AppToolkitDefaults = AppToolkitDefaults(shell = shell, navigation = navigation)
}

/**
 * The shell settings an app may choose as its own defaults. The settings that only exist to test
 * the shell, such as a forced layout, a slowed animation or a start override, stay at the Toolkit's
 * defaults and are left to the developer options.
 */
@AppToolkitDefaultsDsl
class ShellDefaultsBuilder internal constructor(private val from: ShellSettings) {
    var tabTransition: TabTransitionStyle = from.tabTransition
    var backEdgeStyle: BackEdgeStyle = from.backEdgeStyle
    var hideBottomBarOnScroll: Boolean = from.hideBottomBarOnScroll
    var hideTopBarOnScroll: Boolean = from.hideTopBarOnScroll
    var limitContentWidth: Boolean = from.limitContentWidth
    var navigationBarStyle: NavigationBarStyle = from.navigationBarStyle
    var navigationTint: NavigationTint = from.navigationTint

    internal fun build(): ShellSettings = from.copy(
        tabTransition = tabTransition,
        backEdgeStyle = backEdgeStyle,
        hideBottomBarOnScroll = hideBottomBarOnScroll,
        hideTopBarOnScroll = hideTopBarOnScroll,
        limitContentWidth = limitContentWidth,
        navigationBarStyle = navigationBarStyle,
        navigationTint = navigationTint,
    )
}

@AppToolkitDefaultsDsl
class NavigationDefaultsBuilder internal constructor(private val from: NavigationDefaults) {
    var tabTopBarStyle: TopBarStyle = from.tabTopBarStyle
    var childTopBarStyle: TopBarStyle = from.childTopBarStyle
    var pageTopBarStyle: TopBarStyle = from.pageTopBarStyle
    var layoutPolicy: ShellLayoutPolicy = from.layoutPolicy

    internal fun build(): NavigationDefaults = NavigationDefaults(
        tabTopBarStyle = tabTopBarStyle,
        childTopBarStyle = childTopBarStyle,
        pageTopBarStyle = pageTopBarStyle,
        layoutPolicy = layoutPolicy,
    )
}
