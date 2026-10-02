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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants

/**
 * Everything the onboarding theme page renders.
 *
 * @property preferences The stored theme preferences. Until they arrive the page shows the
 * Toolkit's defaults (follow the system, dynamic colors, the default palette) instead of a spinner.
 * @property seasonalThemesUnlocked Whether the holiday palettes are offered all year, as on the
 * theme settings page, once the About screen's easter egg has been found.
 */
@Immutable
data class OnboardingThemeUiState(
    val preferences: ThemePreferencesState = DefaultThemePreferences,
    val seasonalThemesUnlocked: Boolean = false,
)

private val DefaultThemePreferences: ThemePreferencesState = ThemePreferencesState(
    themeMode = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColors = true,
    amoledMode = false,
    dynamicPaletteVariant = 0,
    staticPaletteId = StaticPaletteIds.DEFAULT,
)
