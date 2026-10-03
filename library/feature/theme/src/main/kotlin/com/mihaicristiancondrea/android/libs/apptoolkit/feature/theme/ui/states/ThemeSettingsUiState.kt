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

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable

/**
 * What the theme settings page shows.
 *
 * The three fields change together: [preferences] becomes [Loadable.Ready] in the same update that
 * sets the unlock and the weather effect, so the palette rows never open on a partial state.
 *
 * @property preferences The stored theme preferences.
 * @property seasonalThemesUnlocked Whether the About screen easter egg was found, which keeps the
 * Christmas and Halloween palettes in the palette list all year, and offers the weather effect
 * menu in the app bar.
 * @property weatherEffect What falls over the app.
 */
@Immutable
data class ThemeSettingsUiState(
    val preferences: Loadable<ThemePreferencesState> = Loadable.Loading,
    val seasonalThemesUnlocked: Boolean = false,
    val weatherEffect: WeatherEffect = WeatherEffect.Automatic,
)
