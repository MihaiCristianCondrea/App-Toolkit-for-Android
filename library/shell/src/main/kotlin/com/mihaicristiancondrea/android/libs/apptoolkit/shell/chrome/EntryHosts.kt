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

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.FabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ArticleTopBarHost

/**
 * What the tab screens declare into the shell's scaffold, by screen entry: their floating action
 * buttons and their article bar.
 *
 * A stable holder, so the bodies it is passed to can skip; a plain mutable map is unstable. Its
 * contents need no snapshot state: a screen takes its hosts once, in `remember`.
 */
@Stable
internal class EntryHosts {
    private val fabHosts: MutableMap<String, FabHost> = mutableMapOf()
    private val articleHosts: MutableMap<String, ArticleTopBarHost> = mutableMapOf()

    /** The floating action button host for [entryKey], created the first time it is asked for. */
    fun fabHostFor(entryKey: String): FabHost = fabHosts.getOrPut(entryKey) { FabHost() }

    /** The article bar host for [entryKey], created the first time it is asked for. */
    fun articleHostFor(entryKey: String): ArticleTopBarHost = articleHosts.getOrPut(entryKey) { ArticleTopBarHost() }

    /** Drops the hosts of every screen not in [liveEntryKeys]. */
    fun retainOnly(liveEntryKeys: Set<String>) {
        fabHosts.keys.retainAll(liveEntryKeys)
        articleHosts.keys.retainAll(liveEntryKeys)
    }
}
