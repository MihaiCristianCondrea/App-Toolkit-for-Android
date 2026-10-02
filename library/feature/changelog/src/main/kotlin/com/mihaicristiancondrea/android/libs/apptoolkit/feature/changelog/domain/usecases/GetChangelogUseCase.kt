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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.extensions.extractChangesForVersion

/**
 * Loads the host application's changelog and selects the most useful Markdown to display.
 *
 * The current-version section is preferred. When the API returns valid Markdown without that
 * section, the full history is returned so a version-format mismatch never hides useful content.
 * A failure of the repository passes through unchanged.
 */
class GetChangelogUseCase(
    private val repository: ChangelogRepository,
    private val buildInfoProvider: BuildInfoProvider,
) {

    /** Returns the current version's changes, else the full history, else an empty string. */
    suspend operator fun invoke(): String {
        val history = repository.getChangelog(packageName = buildInfoProvider.packageName).trim()
        val currentVersion = history.extractChangesForVersion(version = buildInfoProvider.appVersion)
        return currentVersion.ifBlank { history }
    }
}
