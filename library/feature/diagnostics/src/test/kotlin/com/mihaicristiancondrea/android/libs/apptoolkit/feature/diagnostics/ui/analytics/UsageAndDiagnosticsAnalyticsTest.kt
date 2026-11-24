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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UsageAndDiagnosticsAnalyticsTest {

    @Test
    fun `screen identity uses UsageAndDiagnostics and UsageAndDiagnosticsScreen`() {
        assertEquals(expected = "UsageAndDiagnostics", actual = USAGE_DIAGNOSTICS_SCREEN.name)
        assertEquals(expected = "UsageAndDiagnosticsScreen", actual = USAGE_DIAGNOSTICS_SCREEN.className)
    }

    @Test
    fun `diagnosticsToggleEvent formats preference toggle with enabled state`() {
        val event = diagnosticsToggleEvent(enabled = true)

        assertEquals(expected = SettingsAnalytics.Events.PREFERENCE_TOGGLE, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = USAGE_DIAGNOSTICS_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = UsageAndDiagnosticsPreferenceKeys.USAGE_AND_DIAGNOSTICS),
            actual = event.params[SettingsAnalytics.Params.PREFERENCE_KEY],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = "true"),
            actual = event.params[SettingsAnalytics.Params.ENABLED],
        )
    }

    @Test
    fun `diagnosticsPrivacyChoicesEvent formats preference view for privacy choices`() {
        val event = diagnosticsPrivacyChoicesEvent()

        assertEquals(expected = SettingsAnalytics.Events.PREFERENCE_VIEW, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = USAGE_DIAGNOSTICS_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = UsageAndDiagnosticsPreferenceKeys.PRIVACY_CHOICES),
            actual = event.params[SettingsAnalytics.Params.PREFERENCE_KEY],
        )
    }
}
