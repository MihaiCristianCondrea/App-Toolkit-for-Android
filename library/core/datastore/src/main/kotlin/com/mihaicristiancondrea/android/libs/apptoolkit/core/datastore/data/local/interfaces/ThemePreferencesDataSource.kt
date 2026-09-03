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
 * Persisted appearance preferences: theme mode, AMOLED, and palette selection.
 */
interface ThemePreferencesDataSource {

    /** Emits the stored theme mode, defaulting to "follow system". */
    val themeMode: Flow<String>

    /** Emits whether the AMOLED (true-black) variant is enabled. */
    val amoledMode: Flow<Boolean>

    /** Emits whether Material You dynamic colors are enabled. */
    val dynamicColors: Flow<Boolean>

    /** Emits the selected dynamic palette variant, clamped to a supported index. */
    val dynamicPaletteVariant: Flow<Int>

    /** Emits the selected static palette id, sanitized to a known palette. */
    val staticPaletteId: Flow<String>

    /** Persists the theme mode. */
    suspend fun saveThemeMode(mode: String)

    /** Persists the AMOLED preference. */
    suspend fun saveAmoledMode(isChecked: Boolean)

    /** Persists the dynamic-colors preference. */
    suspend fun saveDynamicColors(isChecked: Boolean)

    /** Persists the dynamic palette variant, clamped to a supported index. */
    suspend fun saveDynamicPaletteVariant(variant: Int)

    /** Persists the static palette id, sanitized to a known palette. */
    suspend fun saveStaticPaletteId(id: String)
}
