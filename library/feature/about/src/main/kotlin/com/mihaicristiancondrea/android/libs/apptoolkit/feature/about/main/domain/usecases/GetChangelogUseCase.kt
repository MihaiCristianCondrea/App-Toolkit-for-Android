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

/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.extractChangesForVersion
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Loads the host application's changelog and selects the most useful Markdown to display.
 *
 * The current-version section is preferred. When the API returns valid Markdown without that
 * section, the full history is returned so a version-format mismatch never hides useful content.
 */
class GetChangelogUseCase(
    private val repository: ChangelogRepository,
    private val buildInfoProvider: BuildInfoProvider,
) {

    /** Returns current-version changes, full history, or a domain error. */
    operator fun invoke(): Flow<DataState<String, Errors>> =
        repository.fetchChangelog(packageName = buildInfoProvider.packageName)
            .map { result ->
                when (result) {
                    is DataState.Success -> {
                        val history = result.data.trim()
                        val currentVersion = history.extractChangesForVersion(
                            version = buildInfoProvider.appVersion,
                        )
                        DataState.Success(
                            data = currentVersion.ifBlank { history },
                        )
                    }

                    is DataState.Error -> DataState.Error(
                        data = result.data,
                        error = result.error,
                    )

                    is DataState.Loading -> DataState.Loading(data = result.data)
                }
            }
}
