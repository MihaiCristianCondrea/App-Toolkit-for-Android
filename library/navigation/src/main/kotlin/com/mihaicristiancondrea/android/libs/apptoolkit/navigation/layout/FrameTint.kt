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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.runtime.Immutable

/**
 * How far the frame's shared colour should go, from `0` to `1`, and whether to jump there, as it
 * does while a large app bar blends as it collapses, rather than ease there.
 */
@Immutable
data class FrameTint(val amount: Float, val snap: Boolean = false) {
    companion object {
        /** Content at rest under its app bar: the frame keeps the surface colour. */
        val None: FrameTint = FrameTint(amount = 0f)

        /** Content scrolled under its app bar: the frame takes the shared colour. */
        val Full: FrameTint = FrameTint(amount = 1f)
    }
}
