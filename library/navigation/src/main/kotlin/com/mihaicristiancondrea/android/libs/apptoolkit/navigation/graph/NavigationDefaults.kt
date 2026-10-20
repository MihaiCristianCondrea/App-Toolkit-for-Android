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
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy

/**
 * An app's own navigation defaults, used wherever a destination or the host declares none.
 *
 * A destination's own `topBar` always wins over these, so a page registered with
 * [TopBarStyle.Hidden] stays hidden whatever [pageTopBarStyle] says. The Toolkit's own pages
 * declare no style, so they follow the app's. Only the developer options' top bar override replaces
 * a destination's own style.
 */
@Immutable
data class NavigationDefaults(
    val tabTopBarStyle: TopBarStyle = TopBarStyle.Small,
    val childTopBarStyle: TopBarStyle = TopBarStyle.Small,
    val pageTopBarStyle: TopBarStyle = TopBarStyle.Large,
    /** The layout policy `ShellHost` uses when the host passes none. */
    val layoutPolicy: ShellLayoutPolicy = ShellLayoutPolicy(),
)
