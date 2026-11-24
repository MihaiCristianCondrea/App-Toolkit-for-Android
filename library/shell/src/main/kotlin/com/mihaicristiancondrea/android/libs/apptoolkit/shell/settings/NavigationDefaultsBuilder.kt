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

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.NavigationDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy

/**
 * The navigation defaults an app may choose in `appToolkitDefaults { navigation { } }`: the app bar
 * style of each kind of destination and the layout policy. Anything left out keeps the Toolkit's
 * default.
 */
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
