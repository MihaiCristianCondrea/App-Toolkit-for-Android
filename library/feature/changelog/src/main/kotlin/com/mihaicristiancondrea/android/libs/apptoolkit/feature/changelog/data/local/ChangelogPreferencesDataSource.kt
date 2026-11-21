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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.local

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlinx.coroutines.flow.Flow

/**
 * Persisted changelog state for hosts.
 * Reads and writes report storage failures as [StorageException].
 */
interface ChangelogPreferencesDataSource {
    /** Observes the last acknowledged version, using [default] before the first write. */
    fun lastSeenVersion(default: String = ""): Flow<String>
    /** Observes the cached release notes, using [default] before the first write. */
    fun cachedChangelog(default: String = ""): Flow<String>
    /** Persists the host's last acknowledged release version. */
    suspend fun saveLastSeenVersion(version: String)
    /** Replaces the cached release notes with [changelog]. */
    suspend fun saveCachedChangelog(changelog: String)
}
