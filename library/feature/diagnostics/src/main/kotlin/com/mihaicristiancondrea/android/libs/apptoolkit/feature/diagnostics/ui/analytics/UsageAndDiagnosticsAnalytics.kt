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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue

internal const val USAGE_DIAGNOSTICS_SCREEN_NAME = "UsageAndDiagnostics"

internal object UsageAndDiagnosticsPreferenceKeys {
    const val USAGE_AND_DIAGNOSTICS: String = "usage_and_diagnostics"
    const val PRIVACY_CHOICES: String = "advanced_privacy_settings"
}

internal fun diagnosticsToggleEvent(enabled: Boolean): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_TOGGLE,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
            USAGE_DIAGNOSTICS_SCREEN_NAME
        ),
        SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
            UsageAndDiagnosticsPreferenceKeys.USAGE_AND_DIAGNOSTICS
        ),
        SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(enabled.toString()),
    ),
)

internal fun diagnosticsPrivacyChoicesEvent(): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
            USAGE_DIAGNOSTICS_SCREEN_NAME
        ),
        SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
            UsageAndDiagnosticsPreferenceKeys.PRIVACY_CHOICES
        ),
    ),
)
