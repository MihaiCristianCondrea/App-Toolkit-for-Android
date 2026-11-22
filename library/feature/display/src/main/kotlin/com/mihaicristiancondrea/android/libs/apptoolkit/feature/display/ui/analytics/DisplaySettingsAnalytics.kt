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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

internal const val DISPLAY_SETTINGS_SCREEN_NAME: String = "DisplaySettings"

internal val DISPLAY_SETTINGS_SCREEN: TrackedScreen = TrackedScreen(
    name = DISPLAY_SETTINGS_SCREEN_NAME,
    className = "DisplaySettingsScreen",
)

private const val PARAM_VALUE: String = "value"

internal fun displayActionGa4Event(actionName: String, preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(value = actionName),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = preferenceKey),
        ),
    )
}

internal fun displayActionEvent(
    actionName: String,
    preferenceKey: String,
    value: String? = null,
): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = buildMap {
            put(key = SettingsAnalytics.Params.SCREEN, value = AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME))
            put(key = SettingsAnalytics.Params.ACTION_NAME, value = AnalyticsValue.Str(value = actionName))
            put(key = SettingsAnalytics.Params.PREFERENCE_KEY, value = AnalyticsValue.Str(value = preferenceKey))
            if (value != null) {
                put(key = PARAM_VALUE, value = AnalyticsValue.Str(value = value))
            }
        },
    )
}

internal fun displayThemeModeEvent(mode: String): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.THEME_SWITCH,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME),
        SettingsAnalytics.Params.THEME_MODE to AnalyticsValue.Str(value = mode),
    ),
)
