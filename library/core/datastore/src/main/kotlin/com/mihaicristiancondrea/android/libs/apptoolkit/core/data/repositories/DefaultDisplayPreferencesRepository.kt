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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.DisplayPreferencesDataSource
import kotlinx.coroutines.flow.Flow

/** Reads and writes display preferences through the preference store. */
class DefaultDisplayPreferencesRepository(
    private val preferences: DisplayPreferencesDataSource,
) : DisplayPreferencesRepository {

    override val showBottomBarLabels: Flow<Boolean> = preferences.showBottomBarLabels

    override val bouncyButtons: Flow<Boolean> = preferences.bouncyButtons

    override val language: Flow<String> = preferences.language

    override fun startupPage(default: String): Flow<String> = preferences.startupPage(default = default)

    override suspend fun setShowBottomBarLabels(show: Boolean) {
        preferences.saveShowBottomBarLabels(show)
    }

    override suspend fun setBouncyButtons(enabled: Boolean) {
        preferences.saveBouncyButtons(enabled)
    }

    override suspend fun setLanguage(language: String) {
        preferences.saveLanguage(language)
    }

    override suspend fun setStartupPage(route: String) {
        preferences.saveStartupPage(route)
    }
}
