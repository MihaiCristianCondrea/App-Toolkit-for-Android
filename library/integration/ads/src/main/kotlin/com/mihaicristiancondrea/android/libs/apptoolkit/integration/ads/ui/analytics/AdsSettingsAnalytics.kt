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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

internal const val ADS_SETTINGS_SCREEN_NAME: String = "AdsSettings"

private const val ADS_SETTINGS_SCREEN_CLASS: String = "AdsSettingsScreen"

internal val ADS_SETTINGS_SCREEN: TrackedScreen = TrackedScreen(
    name = ADS_SETTINGS_SCREEN_NAME,
    className = ADS_SETTINGS_SCREEN_CLASS,
)

/** The `screen` and `preference_key` parameters every ads settings GA4 event carries. */
private fun adsPreferenceParams(preferenceKey: String): Map<String, AnalyticsValue> = mapOf(
    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = ADS_SETTINGS_SCREEN_NAME),
    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = preferenceKey),
)

internal fun adsLearnMoreEvent(): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.LEARN_MORE),
)

internal fun adsToggleEvent(enabled: Boolean, preferenceKey: String): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.PREFERENCE_TOGGLE,
    params = adsPreferenceParams(preferenceKey = preferenceKey) +
        (SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(value = enabled.toString())),
)

internal fun adsPrivacyPreferenceEvent(): Ga4EventData = Ga4EventData(
    name = SettingsAnalytics.Events.PREFERENCE_VIEW,
    params = adsPreferenceParams(preferenceKey = AdsPreferenceKeys.PERSONALIZED_ADS),
)
