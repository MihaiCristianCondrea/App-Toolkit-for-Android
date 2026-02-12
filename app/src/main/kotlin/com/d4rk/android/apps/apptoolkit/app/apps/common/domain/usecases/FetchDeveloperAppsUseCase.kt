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

package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class FetchDeveloperAppsUseCase(
    private val repository: DeveloperAppsRepository,
    private val firebaseController: FirebaseController,
) {
    /**
     * Returns developer apps.
     *
     * The repository flow is emitted untouched so upstream errors and cancellations keep their
     * original semantics. Loading UI should be driven by the ViewModel using `onStart` to keep
     * presentation concerns out of the domain layer.
     */
    operator fun invoke(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        firebaseController.logBreadcrumb(
            message = "Fetch developer apps started",
            attributes = mapOf("source" to "FetchDeveloperAppsUseCase"),
        )
        emitAll(repository.fetchDeveloperApps())
    }.onStart { // TODO: Search all use cases with onStart that includes the firebaseController.logBreadcrumb and place this log firebaseController.logBreadcrumb into the flow of the view model.
        firebaseController.logBreadcrumb(
            message = "Fetch developer apps collecting",
            attributes = mapOf("stage" to "onStart"),
        )
    }
}
