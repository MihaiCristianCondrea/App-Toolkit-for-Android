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
