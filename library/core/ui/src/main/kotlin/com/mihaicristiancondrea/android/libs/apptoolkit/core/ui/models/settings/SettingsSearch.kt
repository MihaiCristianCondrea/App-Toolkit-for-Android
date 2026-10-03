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
 * [settingsSearchProvider] builds one with the page's section and destination given once.
 *
 * Bind each with a name of its own, `single<SettingsSearchProvider>(named("reader")) { ... }`:
 * Koin keeps only the last of two bindings with the same name, or with none. Binding the name of a
 * Toolkit page's provider (`display`, `theme`, `privacy`, `about`, `advanced`) replaces its rows.
 *
 * [entries] receives the app's declared shell as a [SettingsSearchContext], so a page lists the
 * rows it shows in this app, under the same conditions as the page: a bottom bar's options only
 * where the app uses bottom navigation. Rows whose destination the graph does not register are
 * left out of the results.
 */
fun interface SettingsSearchProvider {
    fun entries(context: SettingsSearchContext): List<SettingsSearchEntry>
}
