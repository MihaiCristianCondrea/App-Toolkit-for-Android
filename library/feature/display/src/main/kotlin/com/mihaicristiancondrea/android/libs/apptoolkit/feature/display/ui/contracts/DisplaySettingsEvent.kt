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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts

/** What the display settings screen asks its ViewModel to do. */
sealed interface DisplaySettingsEvent {
    /** Follows the stored preferences, on start and on retry. */
    data object Load : DisplaySettingsEvent

    /** Stores the selected theme mode key. */
    data class ThemeModeChanged(val mode: String) : DisplaySettingsEvent

    /** Updates whether Material You dynamic colors are enabled. */
    data class DynamicColorsChanged(val enabled: Boolean) : DisplaySettingsEvent

    /** Updates whether button press bounce animations are enabled. */
    data class BouncyButtonsChanged(val enabled: Boolean) : DisplaySettingsEvent

    /** Updates whether bottom navigation bar labels are shown. */
    data class BottomBarLabelsChanged(val show: Boolean) : DisplaySettingsEvent

    /** Stores the selected application language tag. */
    data class LanguageChanged(val language: String) : DisplaySettingsEvent

    /** Stores the selected startup destination route key. */
    data class StartupRouteChanged(val route: String) : DisplaySettingsEvent
}
