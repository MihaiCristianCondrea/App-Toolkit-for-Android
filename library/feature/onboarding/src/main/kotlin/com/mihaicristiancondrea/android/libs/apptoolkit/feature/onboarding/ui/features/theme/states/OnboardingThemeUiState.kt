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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.features.theme.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.ThemePreferencesState

/**
 * Everything the onboarding theme page renders.
 *
 * @property preferences The stored theme preferences. Until they arrive the page shows the
 * Toolkit's default [ThemePreferencesState] instead of a spinner.
 * @property seasonalThemesUnlocked Whether the holiday palettes are offered all year, as on the
 * theme settings page, once the About screen's easter egg has been found.
 */
@Immutable
data class OnboardingThemeUiState(
    val preferences: ThemePreferencesState = ThemePreferencesState(),
    val seasonalThemesUnlocked: Boolean = false,
)
