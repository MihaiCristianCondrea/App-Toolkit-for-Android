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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Emits the stored theme preferences as one [ThemePreferencesState].
 *
 * The first emission is the stored state. Each source flow already fills in its own default for a
 * missing key, so nothing is emitted ahead of them. This used to start the source flows with
 * placeholder defaults, which made the first state claim wallpaper colors and the default palette
 * for a moment; anything acting on the first state, such as the theme page scrolling its palette
 * row to the selection, acted on the placeholder. [themeModeDefault] and [staticPaletteIdDefault]
 * replace blank stored values.
 */
fun ThemePreferencesDataSource.themePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): Flow<ThemePreferencesState> = combine(
    themeMode,
    dynamicColors,
    amoledMode,
    dynamicPaletteVariant,
    staticPaletteId,
) { themeModeValue, dynamicColorsValue, amoledModeValue, dynamicPaletteVariantValue, staticPaletteIdValue ->
    ThemePreferencesState(
        themeMode = themeModeValue.ifBlank { themeModeDefault },
        dynamicColors = dynamicColorsValue,
        amoledMode = amoledModeValue,
        dynamicPaletteVariant = dynamicPaletteVariantValue,
        staticPaletteId = StaticPaletteIds.sanitize(staticPaletteIdValue).ifBlank {
            StaticPaletteIds.sanitize(staticPaletteIdDefault)
        },
    )
}
