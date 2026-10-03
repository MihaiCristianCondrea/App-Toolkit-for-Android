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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states

/** Filters available in the Apps List chip row. */
enum class AppsListFilter {
    All,
    Installed,
    NotInstalled,
    Favorites,
}

/**
 * Whether this filter would match anything, given how many apps, installed apps and favorites
 * there are.
 *
 * The one rule for both places that need it: the chip row shows only these filters, and the
 * ViewModel falls back to [AppsListFilter.All] when the selected one stops being one of them.
 */
fun AppsListFilter.isAvailable(appCount: Int, installedCount: Int, favoritesCount: Int): Boolean =
    when (this) {
        AppsListFilter.All -> true
        AppsListFilter.Installed -> installedCount > 0
        AppsListFilter.NotInstalled -> installedCount in 1..<appCount
        AppsListFilter.Favorites -> favoritesCount > 0
    }
