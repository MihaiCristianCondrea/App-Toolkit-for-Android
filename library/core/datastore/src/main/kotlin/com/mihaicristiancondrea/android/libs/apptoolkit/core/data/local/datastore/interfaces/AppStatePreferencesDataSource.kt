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
 * One-shot app-state flags and timestamps that do not belong to a single feature.
 */
interface AppStatePreferencesDataSource {

    /** Emits the last-used timestamp, or 0 when the app has never recorded one. */
    val lastUsed: Flow<Long>

    /** Emits whether the user has ever opened a settings screen. */
    val settingsInteracted: Flow<Boolean>

    /** Emits whether the components showcase has been unlocked. */
    val componentsShowcaseUnlocked: Flow<Boolean>

    /** Persists the last-used timestamp. */
    suspend fun saveLastUsed(timestamp: Long)

    /** Records that the user has opened a settings screen. */
    suspend fun markSettingsInteracted()

    /** Persists whether the components showcase is unlocked. */
    suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean)
}
