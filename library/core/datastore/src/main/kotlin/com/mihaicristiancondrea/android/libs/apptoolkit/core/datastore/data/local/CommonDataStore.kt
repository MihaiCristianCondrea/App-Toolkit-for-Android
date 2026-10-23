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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants

/**
 * The toolkit's single `settings` Preferences DataStore file, shared by the preference data
 * sources of every module. `dataStoreModule` binds it as `DataStore<Preferences>`; a module keeps
 * its own source over it, with its own keys, in its `data/local` package.
 *
 * Migrations run here rather than as work a data source does when constructed, so they finish
 * before the first read is served, including reads from sources owned by other modules.
 *
 * A file that can no longer be parsed is replaced with empty preferences, so every value falls back
 * to its default and the user sees first-run state again. Without the handler DataStore throws
 * `CorruptionException` from every read, and several of those reads run at startup outside any
 * catch, so one damaged file would crash the app on every launch until its data was cleared.
 */
val Context.commonDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreNamesConstants.DATA_STORE_SETTINGS,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
    produceMigrations = { commonDataStoreMigrations() },
)
