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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState

/**
 * What the theme settings page shows.
 *
 * @property preferences The stored theme preferences.
 * @property seasonalThemesUnlocked Whether the About screen easter egg was found, which keeps the
 * Christmas and Halloween palettes in the palette list all year.
 */
data class ThemeSettingsUiState(
    val preferences: ThemePreferencesState,
    val seasonalThemesUnlocked: Boolean,
)
