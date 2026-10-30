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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppInstallInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

/**
 * Everything the "More apps" section renders.
 *
 * @property apps The developer's other apps. A saved copy shown because the download failed is
 * `Loadable.Ready(stale = true)`.
 * @property installedPackages Which of [apps] are installed on this device.
 * @property favorites The packages marked as favorites, shown in the details sheet.
 * @property selectedApp The app whose details sheet is open, or null when it is closed.
 * @property selectedAppDetails The full metadata of [selectedApp]; `Loadable.Empty` while none is
 * selected.
 * @property selectedAppInstallInfo Whether [selectedApp] is installed and at which version, or null
 * while unknown.
 */
@Immutable
data class MoreAppsUiState(
    val apps: Loadable<ImmutableList<AppSummary>> = Loadable.Loading,
    val installedPackages: ImmutableSet<String> = persistentSetOf(),
    val favorites: ImmutableSet<String> = persistentSetOf(),
    val selectedApp: AppSummary? = null,
    val selectedAppDetails: Loadable<AppDetails> = Loadable.Empty(),
    val selectedAppInstallInfo: AppInstallInfo? = null,
)
