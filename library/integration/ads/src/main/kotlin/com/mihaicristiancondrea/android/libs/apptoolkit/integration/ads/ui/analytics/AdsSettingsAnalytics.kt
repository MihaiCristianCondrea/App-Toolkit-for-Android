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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics

internal const val ADS_SETTINGS_SCREEN_NAME = "AdsSettings"

internal object AdsPreferenceKeys {
    const val DISPLAY_ADS: String = "display_ads"
    const val REDUCE_ADS: String = "reduce_ads"
    const val PERSONALIZED_ADS: String = "personalized_ads"
    const val LEARN_MORE: String = "learn_more"
}

/** The `screen` and `preference_key` parameters every ads settings GA4 event carries. */
private fun adsPreferenceParams(preferenceKey: String): Map<String, AnalyticsValue> = mapOf(
    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(ADS_SETTINGS_SCREEN_NAME),
    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
)

internal fun adsLearnMoreEvent(): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.LEARN_MORE),
)

internal fun adsToggleEvent(enabled: Boolean, preferenceKey: String): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_TOGGLE,
    params = adsPreferenceParams(preferenceKey = preferenceKey) +
        (SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(enabled.toString())),
)

internal fun adsPrivacyPreferenceEvent(): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.PERSONALIZED_ADS),
)
