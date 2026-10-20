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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AppToolkitDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.appToolkitDefaults

/**
 * How the sample behaves out of the box. `appGraph` takes the navigation half and `MainActivity`
 * gives the shell half to `ShellHost`.
 *
 * These restate the Toolkit's own defaults, so the sample looks as it always has; change one to
 * see the whole app follow it. Choices saved in the developer options still win, and resetting them
 * returns here.
 */
val AppDefaults: AppToolkitDefaults = appToolkitDefaults {
    shell {
        backEdgeStyle = BackEdgeStyle.System
        navigationTint = NavigationTint.Always
    }
    navigation {
        pageTopBarStyle = TopBarStyle.Large
    }
}
