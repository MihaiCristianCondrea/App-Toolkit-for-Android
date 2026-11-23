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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.ui.layout.layout
import androidx.navigation3.scene.Scene
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.topShellInfo

/**
 * The transition of the destination on top of [this] scene, or the graph's page transition when
 * the scene carries no shell entry. Shared by the tabs' display and the pages' display in
 * `ShellHost`.
 */
internal fun Scene<*>.transition(graph: ShellGraph, layout: ShellLayoutMode): ScreenTransition {
    val info = topShellInfo ?: return graph.transitions.pages
    return graph.transitions.resolve(info.kind, info.transition, layout)
}
