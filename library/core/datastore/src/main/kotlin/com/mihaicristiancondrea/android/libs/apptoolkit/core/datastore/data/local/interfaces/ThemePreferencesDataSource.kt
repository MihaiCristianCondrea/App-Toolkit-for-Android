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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Persisted appearance preferences: theme mode, AMOLED, and palette selection.
 */
interface ThemePreferencesDataSource {

    /** Emits the stored theme mode, defaulting to "follow system". */
    val themeMode: Flow<String>

    val amoledMode: Flow<Boolean>

    val dynamicColors: Flow<Boolean>

    /** Emits the selected dynamic palette variant, clamped to a supported index. */
    val dynamicPaletteVariant: Flow<Int>

    /** Emits the selected static palette id, sanitized to a known palette. */
    val staticPaletteId: Flow<String>

    /**
     * Emits every stored value at once, each as its own flow above reports it.
     *
     * Combining the separate flows reports a write that changes two values, such as a palette
     * choice, as two emissions with a mixed state in between. `DefaultThemePreferencesDataSource`
     * reads all of them from one snapshot of storage, so each write is one emission; this default
     * combines the flows so other implementations keep compiling.
     */
    val storedPreferences: Flow<ThemePreferencesState>
        get() = combine(
            themeMode,
            dynamicColors,
            amoledMode,
            dynamicPaletteVariant,
            staticPaletteId,
        ) { themeMode, dynamicColors, amoledMode, dynamicPaletteVariant, staticPaletteId ->
            ThemePreferencesState(
                themeMode = themeMode,
                dynamicColors = dynamicColors,
                amoledMode = amoledMode,
                dynamicPaletteVariant = dynamicPaletteVariant,
                staticPaletteId = staticPaletteId,
            )
        }

    suspend fun saveThemeMode(mode: String)

    suspend fun saveAmoledMode(isChecked: Boolean)

    suspend fun saveDynamicColors(isChecked: Boolean)

    /** Persists the dynamic palette variant, clamped to a supported index. */
    suspend fun saveDynamicPaletteVariant(variant: Int)

    /** Persists the static palette id, sanitized to a known palette. */
    suspend fun saveStaticPaletteId(id: String)

    /**
     * Persists a palette choice: whether dynamic colors are on, plus the palette that goes with
     * it. A null value is left unchanged.
     *
     * A palette choice always changes [dynamicColors] together with a palette, and two separate
     * writes would let a process death keep only the first one. `DefaultThemePreferencesDataSource`
     * writes everything in one transaction; this default writes the values one by one so other
     * implementations keep compiling.
     */
    suspend fun savePalette(
        dynamicColors: Boolean,
        dynamicPaletteVariant: Int? = null,
        staticPaletteId: String? = null,
    ) {
        saveDynamicColors(dynamicColors)
        dynamicPaletteVariant?.let { variant -> saveDynamicPaletteVariant(variant) }
        staticPaletteId?.let { id -> saveStaticPaletteId(id) }
    }
}
