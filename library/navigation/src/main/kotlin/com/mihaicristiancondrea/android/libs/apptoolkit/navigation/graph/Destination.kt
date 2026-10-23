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

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import kotlin.reflect.KClass

/** Everything the shell needs to know to show one kind of [NavKey]. */
@Immutable
class Destination<K : NavKey> @PublishedApi internal constructor(
    val keyClass: KClass<K>,
    val kind: DestinationKind,
    val topBar: TopBarStyle,
    val paneRole: PaneRole,
    val contentWidth: ContentWidth,
    /**
     * Whether the shell wraps [content] in a page frame titled [title]. Only pages use it; tabs
     * and children are always drawn under the shell's own app bar.
     */
    val scaffold: Boolean,
    val title: (@Composable (K) -> String)?,
    val actions: (@Composable RowScope.(K) -> Unit)?,
    val content: @Composable (K) -> Unit,
    /** How this destination enters and leaves; null takes its kind's default from [ShellGraph.transitions]. */
    val transition: ScreenTransition? = null,
    /**
     * A floating action button shown while this destination is on top: above the navigation bar,
     * the banner and the mini player on a tab or child, in the page's frame on a page.
     */
    val floatingActionButton: (@Composable (K) -> Unit)? = null,
    /**
     * Floating action buttons described rather than drawn, shown in the same place as
     * [floatingActionButton], as a column: the shell draws each with the Toolkit's buttons. A
     * screen can add its own with `ScaffoldFabs` from `:library:core:ui`.
     */
    val floatingActionButtons: (@Composable (K) -> List<ToolkitFab>)? = null,
    /**
     * For a [PaneRole.List] page: what the detail side shows before anything is chosen. Null takes
     * the shell's own placeholder.
     */
    val placeholder: (@Composable () -> Unit)? = null,
    /** How the app bar the shell draws for this destination answers scrolling; see [TopBarScrollMode]. */
    val topBarScrollMode: TopBarScrollMode = TopBarScrollMode.Default,
    /** Whether the app bar the shell draws for this page shows a back arrow. Only titled pages use it. */
    val backButton: Boolean = true,
)
