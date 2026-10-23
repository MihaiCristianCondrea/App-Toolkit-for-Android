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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

internal const val SETTINGS_SCREEN_NAME = "Settings"

internal object SettingsActionNames {
    const val RETRY_LOAD: String = "retry_load"
}

internal fun settingsActionGa4Event(actionName: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}

internal const val UNKNOWN_PREFERENCE_KEY = "unknown"

internal fun settingsPreferenceTapEvent(preferenceKey: String?): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
            SETTINGS_SCREEN_NAME
        ),
        SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
            preferenceKey ?: UNKNOWN_PREFERENCE_KEY
        ),
    ),
)
