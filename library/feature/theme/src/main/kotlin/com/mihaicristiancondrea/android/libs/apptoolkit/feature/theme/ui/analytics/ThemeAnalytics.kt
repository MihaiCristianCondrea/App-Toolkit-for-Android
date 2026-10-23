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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.WALLPAPER_PALETTE_PAGE

internal const val THEME_SCREEN_NAME = "Theme"

private fun themeEvent(name: String, vararg params: Pair<String, String>): AnalyticsEvent = AnalyticsEvent(
    name = name,
    params = buildMap<String, AnalyticsValue> {
        put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(THEME_SCREEN_NAME))
        params.forEach { (key, value) -> put(key, AnalyticsValue.Str(value)) }
    },
)

internal fun paletteTabSelectEvent(page: Int): AnalyticsEvent = themeEvent(
    "theme_tab_select",
    "tab" to (if (page == WALLPAPER_PALETTE_PAGE) "wallpaper" else "other"),
)

internal fun dynamicPaletteSelectEvent(variant: Int): AnalyticsEvent = themeEvent(
    "theme_palette_select",
    "palette_type" to "dynamic",
    "variant" to variant.toString(),
)

internal fun staticPaletteSelectEvent(id: String, seasonal: Boolean): AnalyticsEvent = themeEvent(
    "theme_palette_select",
    "palette_type" to "static",
    "palette_id" to id,
    "seasonal" to seasonal.toString(),
)

internal fun themeModeSelectEvent(mode: String): AnalyticsEvent = themeEvent(
    SettingsAnalytics.Events.THEME_SWITCH,
    SettingsAnalytics.Params.THEME_MODE to mode,
)

internal fun amoledToggleEvent(enabled: Boolean): AnalyticsEvent = themeEvent(
    "theme_toggle_amoled",
    "enabled" to enabled.toString(),
)

internal fun openDisplaySettingsEvent(opened: Boolean): AnalyticsEvent = themeEvent(
    "theme_open_display_settings",
    "opened" to opened.toString(),
)
