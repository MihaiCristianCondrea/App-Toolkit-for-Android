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

package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** State rendered by the Apps List screen. */
@Immutable
data class AppListUiState(
    val apps: ImmutableList<AppInfo> = persistentListOf(),
    val selectedFilter: AppsListFilter = AppsListFilter.All,
)

/** Filters available in the Apps List chip row. */
enum class AppsListFilter {
    All,
    Installed,
    NotInstalled,
    Favorites,
}
