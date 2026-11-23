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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.storageCall
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Reads and writes the appearance through the preference store. Writes throw
 * [com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException] when the store fails.
 */
class DefaultThemePreferencesRepository(
    private val preferences: ThemePreferencesDataSource,
) : ThemePreferencesRepository {

    override val preferencesState: Flow<ThemePreferencesState> = preferences.themePreferencesState()

    override val themeMode: Flow<String> = preferences.themeMode

    override val dynamicColors: Flow<Boolean> = preferences.dynamicColors

    override suspend fun selectThemeMode(mode: String): Unit = storageCall {
        preferences.saveThemeMode(mode)
        // Use the persisted AMOLED preference because a screen may hold an older snapshot.
        if (mode == DataStoreNamesConstants.THEME_MODE_LIGHT && preferences.amoledMode.first()) {
            preferences.saveAmoledMode(false)
        }
    }

    override suspend fun setAmoledMode(enabled: Boolean): Unit = storageCall {
        preferences.saveAmoledMode(enabled)
    }

    override suspend fun setDynamicColors(enabled: Boolean): Unit = storageCall {
        preferences.saveDynamicColors(enabled)
    }

    override suspend fun selectDynamicPalette(variant: Int): Unit = storageCall {
        preferences.savePalette(dynamicColors = true, dynamicPaletteVariant = variant)
    }

    override suspend fun selectStaticPalette(id: String): Unit = storageCall {
        preferences.savePalette(dynamicColors = false, staticPaletteId = id)
    }
}

/**
 * Emits the stored theme preferences as one [ThemePreferencesState].
 *
 * The first emission is the stored state, so a collector never sees placeholder defaults
 * that it could act on, such as the theme page scrolling its palette row to the selection. The
 * source fills in its own default for a missing key. [themeModeDefault] and
 * [staticPaletteIdDefault] replace blank stored values.
 *
 * It follows [ThemePreferencesDataSource.storedPreferences], so with the Toolkit's data source a
 * write that changes several values, such as a palette choice, arrives as one state.
 */
private fun ThemePreferencesDataSource.themePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): Flow<ThemePreferencesState> = storedPreferences.map { stored ->
    stored.copy(
        themeMode = stored.themeMode.ifBlank { themeModeDefault },
        staticPaletteId = StaticPaletteIds.sanitize(stored.staticPaletteId).ifBlank {
            StaticPaletteIds.sanitize(staticPaletteIdDefault)
        },
    )
}
