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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.theme.ThemePreferencesState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants

/**
 * Collects [themePreferencesState] as state within a composable.
 */
@Composable
fun rememberThemePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColorsDefault: Boolean = true,
    amoledModeDefault: Boolean = false,
    dynamicPaletteVariantDefault: Int = 0,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): ThemePreferencesState {
    val dataStore: CommonDataStore = rememberCommonDataStore()
    return dataStore.themePreferencesState(
        themeModeDefault = themeModeDefault,
        dynamicColorsDefault = dynamicColorsDefault,
        amoledModeDefault = amoledModeDefault,
        dynamicPaletteVariantDefault = dynamicPaletteVariantDefault,
        staticPaletteIdDefault = staticPaletteIdDefault,
    ).collectAsStateWithLifecycle(
        initialValue = ThemePreferencesState(
            themeMode = themeModeDefault,
            dynamicColors = dynamicColorsDefault,
            amoledMode = amoledModeDefault,
            dynamicPaletteVariant = dynamicPaletteVariantDefault,
            staticPaletteId = staticPaletteIdDefault,
        )
    ).value
}
