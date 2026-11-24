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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository

/** Loads the host application's parsed changelog releases with the installed release marked. */
class GetChangelogUseCase(
    private val repository: ChangelogRepository,
    private val buildInfoRepository: BuildInfoRepository,
) {
    /**
     * Returns parsed releases in document order for the installed host package and version.
     *
     * Network failures from the repository propagate to the caller.
     */
    suspend operator fun invoke(): List<ChangelogRelease> = repository.getChangelog(
        packageName = buildInfoRepository.packageName,
        currentVersion = buildInfoRepository.appVersion,
    )
}
