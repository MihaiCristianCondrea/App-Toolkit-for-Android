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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * Persisted changelog state: the last version the user saw and its cached Markdown.
 */
interface ChangelogPreferencesDataSource {

    /** Emits the last app version whose changelog was shown. */
    fun lastSeenVersion(default: String = ""): Flow<String>

    /** Emits the cached changelog Markdown for the last seen version. */
    fun cachedChangelog(default: String = ""): Flow<String>

    /** Persists the last app version whose changelog was shown. */
    suspend fun saveLastSeenVersion(version: String)

    /** Persists the cached changelog Markdown. */
    suspend fun saveCachedChangelog(changelog: String)
}
