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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.themePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.rememberCommonDataStore

/**
 * Collects the application-facing theme preference model at the design-system boundary.
 *
 * The combined flow is remembered because `collectAsStateWithLifecycle` restarts collection
 * whenever it receives a different flow instance. Built inline, every recomposition of the theme
 * root would re-subscribe to DataStore and fall back to the initial value, briefly swapping the
 * whole app's color scheme and recomposing everything under it. The defaults here only fill that
 * initial value, for the frames before the stored preferences arrive.
 */
@Composable
internal fun rememberThemePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColorsDefault: Boolean = true,
    amoledModeDefault: Boolean = false,
    dynamicPaletteVariantDefault: Int = 0,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): ThemePreferencesState {
    val dataStore = rememberCommonDataStore()
    val themePreferences = remember(dataStore, themeModeDefault, staticPaletteIdDefault) {
        dataStore.themePreferences.themePreferencesState(
            themeModeDefault = themeModeDefault,
            staticPaletteIdDefault = staticPaletteIdDefault,
        )
    }
    return themePreferences.collectAsStateWithLifecycle(
        initialValue = ThemePreferencesState(
            themeMode = themeModeDefault,
            dynamicColors = dynamicColorsDefault,
            amoledMode = amoledModeDefault,
            dynamicPaletteVariant = dynamicPaletteVariantDefault,
            staticPaletteId = staticPaletteIdDefault,
        ),
    ).value
}
