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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

/**
 * Lets this app bar drive the shared colour of the navigation beside it, while the person's
 * settings have it follow scrolling, for as long as the bar is composed. It does nothing anywhere
 * else.
 *
 * [tint] is read in a snapshot, so reading the bar's scroll state there keeps the colour in step.
 *
 * @param underneath Whether the bar sits under pages, as the shell's own tabs do: a page shown
 * over it decides the colour even when the tab's bar appears later.
 */
@Composable
fun FollowScrollWithFrameTint(underneath: Boolean = false, tint: () -> FrameTint) {
    val scrollTint = LocalBesideNavigation.current?.scrollTint ?: return
    val latest by rememberUpdatedState(tint)
    DisposableEffect(scrollTint, underneath) {
        val driver: () -> FrameTint = { latest() }
        scrollTint.add(driver, underneath)
        onDispose { scrollTint.remove(driver) }
    }
}
