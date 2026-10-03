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

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInstallInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

/**
 * Everything the Apps List screen renders.
 *
 * @property apps The catalogue. A saved copy shown because the download failed is
 * `Loadable.Ready(stale = true)`.
 * @property installedPackages The catalogue's packages installed on this device.
 * @property favorites The packages the user marked as favorites.
 * @property selectedFilter The chip in use. Always one that matches something, see
 * [AppsListFilter.isAvailable].
 * @property selectedApp The app whose details sheet is open, or null when it is closed.
 * @property selectedAppDetails The full metadata of [selectedApp]. `Loadable.Empty` while no app is
 * selected.
 * @property selectedAppInstallInfo Whether [selectedApp] is installed and at which version, or null
 * while unknown.
 * @property randomAppToOpen An app the random-app button picked. The screen opens it and sends
 * `HomeEvent.RandomAppOpened`, so the flag survives a configuration change but fires once.
 */
@Immutable
data class AppListUiState(
    val apps: Loadable<ImmutableList<AppInfo>> = Loadable.Loading,
    val installedPackages: ImmutableSet<String> = persistentSetOf(),
    val favorites: ImmutableSet<String> = persistentSetOf(),
    val selectedFilter: AppsListFilter = AppsListFilter.All,
    val selectedApp: AppInfo? = null,
    val selectedAppDetails: Loadable<AppDetails> = Loadable.Empty(),
    val selectedAppInstallInfo: AppInstallInfo? = null,
    val randomAppToOpen: AppInfo? = null,
) {
    /** The catalogue being shown, fresh or saved, or an empty list while there is none. */
    val loadedApps: ImmutableList<AppInfo>
        get() = (apps as? Loadable.Ready)?.value ?: persistentListOf()

    /** Whether the random-app button has an app to pick. */
    val canOpenRandomApp: Boolean
        get() = loadedApps.isNotEmpty()
}
