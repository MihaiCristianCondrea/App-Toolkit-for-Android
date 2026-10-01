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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppErrors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Fake implementation of [DeveloperAppsRepository] that returns a predefined list.
 * It can optionally emit an error when [fetchDeveloperApps] is called, carrying [cachedApps] as the
 * saved catalogue, or throw [firstFetchThrowable] from the first fetch only.
 */
class FakeDeveloperAppsRepository(
    private val apps: List<AppSummary>,
    private val fetchError: AppErrors? = null,
    private val detailsError: AppErrors? = null,
    private val cachedApps: List<AppSummary>? = null,
    private var firstFetchThrowable: Throwable? = null,
) : DeveloperAppsRepository {
    override fun fetchDeveloperApps(): Flow<DataState<List<AppSummary>, AppErrors>> = flow {
        firstFetchThrowable?.let { throwable ->
            firstFetchThrowable = null
            throw throwable
        }
        fetchError?.let {
            emit(DataState.Error(data = cachedApps, error = it))
            return@flow
        }
        emit(DataState.Success(apps))
    }

    override suspend fun savedDeveloperApps(): List<AppSummary>? = cachedApps

    override fun fetchAppDetails(
        packageName: String,
    ): Flow<DataState<AppDetails, AppErrors>> = flow {
        detailsError?.let { error ->
            emit(DataState.Error(error = error))
            return@flow
        }
        val app = apps.firstOrNull { it.packageName == packageName }
        if (app == null) {
            emit(DataState.Error(error = AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS))
            return@flow
        }
        emit(
            DataState.Success(
                data = AppDetails(
                    name = app.name,
                    packageName = app.packageName,
                    iconUrl = app.iconUrl,
                    description = app.shortDescription,
                    shortDescription = app.shortDescription,
                    category = app.category,
                ),
            ),
        )
    }
}
