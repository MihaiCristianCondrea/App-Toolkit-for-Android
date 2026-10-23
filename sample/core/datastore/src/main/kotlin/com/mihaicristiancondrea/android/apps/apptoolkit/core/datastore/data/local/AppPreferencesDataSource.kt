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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.Flow

/**
 * The sample's own preferences, kept with its own keys in the Toolkit's shared settings file, and
 * the startup state the main activity reads before the first frame.
 */
interface AppPreferencesDataSource {

    /** Emits true until onboarding is completed. */
    val startup: Flow<Boolean>

    val componentsShowcaseUnlocked: Flow<Boolean>

    /** The stored startup route mapped to a key, falling back to [defaultRoute]. */
    fun <T : NavKey> startupDestinationFlow(
        defaultRoute: String,
        mapToKey: (String) -> T,
    ): Flow<T>

    suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean)
}
