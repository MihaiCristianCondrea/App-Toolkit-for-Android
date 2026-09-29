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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition

/**
 * What the shell attaches to every entry it creates, so scenes and transitions can tell a tab from
 * a child and a list from a detail without knowing the app's keys.
 *
 * @param tabIndex The tab whose stack holds the entry, or -1 for pages.
 * @param transition The destination's own transition, or null for its kind's default.
 */
@Immutable
data class ShellEntryInfo(
    val kind: DestinationKind,
    val tabIndex: Int,
    val paneRole: PaneRole,
    val transition: ScreenTransition? = null,
) {
    object Key : NavMetadataKey<ShellEntryInfo>

    fun toMetadata(): Map<String, Any> = metadata { put(Key, this@ShellEntryInfo) }
}

val NavEntry<*>.shellInfo: ShellEntryInfo?
    get() = metadata[ShellEntryInfo.Key]

val Scene<*>.topShellInfo: ShellEntryInfo?
    get() = entries.lastOrNull()?.shellInfo

/**
 * A page's title and app bar actions, attached to its entry so a scene that shows several pages
 * at once can draw one app bar for all of them.
 */
@Immutable
class PageChrome(
    val title: @Composable () -> String,
    val actions: (@Composable RowScope.() -> Unit)? = null,
) {
    object Key : NavMetadataKey<PageChrome>

    fun toMetadata(): Map<String, Any> = metadata { put(Key, this@PageChrome) }
}

val NavEntry<*>.pageChrome: PageChrome?
    get() = metadata[PageChrome.Key]
