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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * Persisted display preferences: language, startup destination, and interaction chrome.
 */
interface DisplayPreferencesDataSource {

    val showBottomBarLabels: Flow<Boolean>

    val bouncyButtons: Flow<Boolean>

    /** Emits the stored language tag, defaulting to `en`. */
    val language: Flow<String>

    /**
     * Emits the preferred startup route.
     *
     * @param default value emitted when the preference has not been set yet.
     */
    fun startupPage(default: String = ""): Flow<String>

    suspend fun saveShowBottomBarLabels(isChecked: Boolean)

    suspend fun saveBouncyButtons(isChecked: Boolean)

    suspend fun saveLanguage(language: String)

    suspend fun saveStartupPage(route: String)
}
