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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph

/**
 * What a [SettingsSearchProvider] decides its rows from: the app's graph and what its declared
 * shell can do. The settings search builds it from the same `ShellHost` locals the settings pages
 * read, so a page and its search rows follow the same rules.
 *
 * @property graph The app's graph, for rows that depend on which destinations it registers.
 * @property capabilities What the app's graph and layout policy can show, for rows that depend on
 * its navigation, app bars, accessories or content width. By default those of [graph] with the
 * default layout policy, as in tests.
 */
@Immutable
class SettingsSearchContext(
    val graph: ShellGraph,
    val capabilities: ShellCapabilities = ShellCapabilities.of(graph),
)
