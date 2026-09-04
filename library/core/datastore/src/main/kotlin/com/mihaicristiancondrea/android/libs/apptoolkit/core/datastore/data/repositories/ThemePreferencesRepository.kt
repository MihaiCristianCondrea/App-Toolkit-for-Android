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
import kotlinx.coroutines.flow.Flow

/**
 * The way to read and change the app's appearance.
 *
 * State holders talk to this rather than to the preference data source behind it, so the rules that
 * keep the stored appearance coherent live in one place instead of being repeated by every screen
 * that offers the same choice.
 */
interface ThemePreferencesRepository {

    /** The stored appearance, with defaults filled in for anything never set. */
    val preferencesState: Flow<ThemePreferencesState>

    /** Emits the stored theme mode. */
    val themeMode: Flow<String>

    /** Emits whether Material You dynamic colors are enabled. */
    val dynamicColors: Flow<Boolean>

    /**
     * Switches the theme mode.
     *
     * Turns AMOLED off when moving to the light theme, where a true-black surface has no meaning.
     */
    suspend fun selectThemeMode(mode: String)

    /** Turns the AMOLED (true-black) variant on or off. */
    suspend fun setAmoledMode(enabled: Boolean)

    /** Turns Material You dynamic colors on or off. */
    suspend fun setDynamicColors(enabled: Boolean)

    /** Picks a dynamic palette variant, which implies dynamic colors are on. */
    suspend fun selectDynamicPalette(variant: Int)

    /** Picks a static palette, which implies dynamic colors are off. */
    suspend fun selectStaticPalette(id: String)
}
