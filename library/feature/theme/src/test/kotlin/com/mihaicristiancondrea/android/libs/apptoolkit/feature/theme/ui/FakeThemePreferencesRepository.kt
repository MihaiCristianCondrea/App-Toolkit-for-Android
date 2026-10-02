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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * In-memory [ThemePreferencesRepository] for the theme ViewModel tests. Writes change [stored] the
 * way the real repository changes the store, so a test reads the result from the ViewModel state.
 *
 * @param initial The stored appearance, or null for one that has not loaded yet.
 */
class FakeThemePreferencesRepository(
    initial: ThemePreferencesState? = DefaultPreferences,
) : ThemePreferencesRepository {

    /** The stored appearance. Nothing is emitted while it is null. */
    val stored: MutableStateFlow<ThemePreferencesState?> = MutableStateFlow(initial)

    /** Thrown when [preferencesState] is collected, while set. */
    var readFailure: Throwable? = null

    /** Thrown by every write, while set. */
    var writeFailure: Throwable? = null

    override val preferencesState: Flow<ThemePreferencesState> = flow {
        readFailure?.let { throw it }
        emitAll(stored.filterNotNull())
    }

    override val themeMode: Flow<String> = preferencesState.map { it.themeMode }

    override val dynamicColors: Flow<Boolean> = preferencesState.map { it.dynamicColors }

    override suspend fun selectThemeMode(mode: String) = write { copy(themeMode = mode) }

    override suspend fun setAmoledMode(enabled: Boolean) = write { copy(amoledMode = enabled) }

    override suspend fun setDynamicColors(enabled: Boolean) = write { copy(dynamicColors = enabled) }

    override suspend fun selectDynamicPalette(variant: Int) = write {
        copy(dynamicColors = true, dynamicPaletteVariant = variant)
    }

    override suspend fun selectStaticPalette(id: String) = write {
        copy(dynamicColors = false, staticPaletteId = id)
    }

    private fun write(change: ThemePreferencesState.() -> ThemePreferencesState) {
        writeFailure?.let { throw it }
        stored.update { current -> current?.change() }
    }

    companion object {
        val DefaultPreferences: ThemePreferencesState = ThemePreferencesState(
            themeMode = DataStoreNamesConstants.THEME_MODE_DARK,
            dynamicColors = true,
            amoledMode = false,
            dynamicPaletteVariant = 2,
            staticPaletteId = StaticPaletteIds.DEFAULT,
        )
    }
}
