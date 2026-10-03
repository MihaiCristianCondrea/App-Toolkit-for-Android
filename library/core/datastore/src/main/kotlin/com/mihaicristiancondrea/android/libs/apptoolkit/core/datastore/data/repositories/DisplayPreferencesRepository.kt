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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories

import kotlinx.coroutines.flow.Flow

/**
 * The way to read and change display preferences: language, startup destination, and the small
 * interaction settings that go with them.
 */
interface DisplayPreferencesRepository {

    val showBottomBarLabels: Flow<Boolean>

    val bouncyButtons: Flow<Boolean>

    val language: Flow<String>

    /**
     * Emits the route the app opens on.
     *
     * @param default what to emit while no route has been chosen.
     */
    fun startupPage(default: String): Flow<String>

    suspend fun setShowBottomBarLabels(show: Boolean)

    suspend fun setBouncyButtons(enabled: Boolean)

    suspend fun setLanguage(language: String)

    suspend fun setStartupPage(route: String)
}
