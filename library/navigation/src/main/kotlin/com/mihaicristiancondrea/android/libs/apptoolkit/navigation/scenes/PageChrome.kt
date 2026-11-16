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
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarScrollMode

/**
 * A page's title and app bar actions, attached to its entry so a scene that shows several pages
 * at once can draw one app bar for all of them.
 */
@Immutable
class PageChrome(
    val title: @Composable () -> String,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    /** The page's key, to tell whether it stands in for a tab beside the navigation. */
    val key: Any? = null,
    /** How the bar answers the page's scrolling; a static page does not tint the bar across both panes. */
    val topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
) {
    object Key : NavMetadataKey<PageChrome>

    fun toMetadata(): Map<String, Any> = metadata { put(Key, this@PageChrome) }
}
