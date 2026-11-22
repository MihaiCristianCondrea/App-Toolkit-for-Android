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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.constants

/** Operation names, extra keys, and setting identifiers reported by `ThemeSettingsViewModel`. */
internal object ThemeOperationNames {
    const val OBSERVE_PREFERENCES: String = "observePreferences"
    const val PERSIST_THEME_SETTING: String = "persistThemeSetting"

    object ExtraKeys {
        const val SETTING: String = "setting"
    }

    object Settings {
        const val THEME_MODE: String = "theme_mode"
        const val AMOLED_MODE: String = "amoled_mode"
        const val WEATHER_EFFECT: String = "weather_effect"
        const val DYNAMIC_PALETTE: String = "dynamic_palette"
        const val STATIC_PALETTE: String = "static_palette"
    }
}
