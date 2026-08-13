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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.models.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow

/** Domain contract for compact catalog and package-specific application metadata. */
interface DeveloperAppsRepository {

    /** Returns compact summaries for every public application. */
    fun fetchDeveloperApps(): Flow<DataState<List<AppSummary>, AppErrors>>

    /** Returns the full public metadata document for [packageName]. */
    fun fetchAppDetails(packageName: String): Flow<DataState<AppDetails, AppErrors>>
}
