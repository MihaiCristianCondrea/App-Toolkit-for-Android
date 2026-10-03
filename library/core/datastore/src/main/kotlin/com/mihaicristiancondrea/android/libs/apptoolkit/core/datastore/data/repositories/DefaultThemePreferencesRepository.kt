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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.themePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/** Reads and writes the appearance through the preference store. */
class DefaultThemePreferencesRepository(
    private val preferences: ThemePreferencesDataSource,
) : ThemePreferencesRepository {

    override val preferencesState: Flow<ThemePreferencesState> = preferences.themePreferencesState()

    override val themeMode: Flow<String> = preferences.themeMode

    override val dynamicColors: Flow<Boolean> = preferences.dynamicColors

    override suspend fun selectThemeMode(mode: String) {
        preferences.saveThemeMode(mode)
        // Use the persisted AMOLED preference because a screen may hold an older snapshot.
        if (mode == DataStoreNamesConstants.THEME_MODE_LIGHT && preferences.amoledMode.first()) {
            preferences.saveAmoledMode(false)
        }
    }

    override suspend fun setAmoledMode(enabled: Boolean) {
        preferences.saveAmoledMode(enabled)
    }

    override suspend fun setDynamicColors(enabled: Boolean) {
        preferences.saveDynamicColors(enabled)
    }

    override suspend fun selectDynamicPalette(variant: Int) {
        preferences.savePalette(dynamicColors = true, dynamicPaletteVariant = variant)
    }

    override suspend fun selectStaticPalette(id: String) {
        preferences.savePalette(dynamicColors = false, staticPaletteId = id)
    }
}
