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

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph

/**
 * A setting the settings search can find: a row on one of the settings pages, rather than the
 * page itself.
 *
 * @property title The row's own title, as the page shows it.
 * @property section The page it lives on, shown under the title in the results.
 * @property destination Where choosing the result goes: the page that holds the row, or the page
 *   the row itself opens.
 * @property summary The row's summary, searched as well as the title.
 */
@Immutable
data class SettingsSearchEntry(
    @param:StringRes val title: Int,
    @param:StringRes val section: Int,
    val destination: NavKey,
    @param:StringRes val summary: Int? = null,
)

/**
 * The searchable rows one settings page contributes. Each page's module binds one in Koin, and
 * the settings list collects them all with `getAll`, so the list depends on none of the pages.
 *
 * [entries] receives the app's graph, so a page can leave out the rows it only shows for some
 * apps, such as a bottom bar's options in an app without tabs.
 */
fun interface SettingsSearchProvider {
    fun entries(graph: ShellGraph): List<SettingsSearchEntry>
}
