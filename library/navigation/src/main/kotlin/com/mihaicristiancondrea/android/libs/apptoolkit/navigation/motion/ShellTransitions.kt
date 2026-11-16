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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode

/**
 * Which [ScreenTransition] each kind of destination uses when it declares none, with defaults of
 * their own for some navigation layouts.
 *
 * ```
 * transitions(
 *     ShellTransitions(
 *         children = ScreenTransition.Slide,
 *         pages = ScreenTransition.Activity,
 *         byLayout = mapOf(ShellLayoutMode.PermanentDrawer to ShellTransitions(children = ScreenTransition.Fade)),
 *     ),
 * )
 * ```
 *
 * A destination's own `transition` wins over all of these.
 */
@Immutable
class ShellTransitions(
    val children: ScreenTransition = ScreenTransition.Slide,
    val pages: ScreenTransition = ScreenTransition.Activity,
    val byLayout: Map<ShellLayoutMode, ShellTransitions> = emptyMap(),
) {
    /** The transition of a destination of [kind] declaring [own], in [layout]. */
    fun resolve(kind: DestinationKind, own: ScreenTransition?, layout: ShellLayoutMode): ScreenTransition {
        if (own != null) return own
        val defaults = byLayout[layout] ?: this
        return if (kind == DestinationKind.Page) defaults.pages else defaults.children
    }
}
