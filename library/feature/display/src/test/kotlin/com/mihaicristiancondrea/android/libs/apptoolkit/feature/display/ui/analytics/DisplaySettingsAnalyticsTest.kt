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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DisplaySettingsAnalyticsTest {

    @Test
    fun `screen identity uses DisplaySettings and DisplaySettingsScreen`() {
        assertEquals(expected = "DisplaySettings", actual = DISPLAY_SETTINGS_SCREEN.name)
        assertEquals(expected = "DisplaySettingsScreen", actual = DISPLAY_SETTINGS_SCREEN.className)
    }

    @Test
    fun `displayActionEvent includes optional value only when supplied`() {
        val withValue = displayActionEvent(
            actionName = DisplayActionNames.OPEN_LANGUAGE_SETTINGS,
            preferenceKey = DisplayPreferenceKeys.LANGUAGE,
            value = LanguageDestinations.IN_APP_DIALOG,
        )

        assertEquals(expected = SettingsAnalytics.Events.ACTION, actual = withValue.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME),
            actual = withValue.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = DisplayActionNames.OPEN_LANGUAGE_SETTINGS),
            actual = withValue.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = DisplayPreferenceKeys.LANGUAGE),
            actual = withValue.params[SettingsAnalytics.Params.PREFERENCE_KEY],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = LanguageDestinations.IN_APP_DIALOG),
            actual = withValue.params["value"],
        )

        val withoutValue = displayActionEvent(
            actionName = DisplayActionNames.OPEN_STARTUP_DIALOG,
            preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
        )
        assertFalse(actual = withoutValue.params.containsKey(key = "value"))
    }

    @Test
    fun `displayActionGa4Event formats settings action for preference item`() {
        val event = displayActionGa4Event(
            actionName = DisplayActionNames.OPEN_STARTUP_DIALOG,
            preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
        )

        assertEquals(expected = SettingsAnalytics.Events.ACTION, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = DisplayActionNames.OPEN_STARTUP_DIALOG),
            actual = event.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = DisplayPreferenceKeys.STARTUP_PAGE),
            actual = event.params[SettingsAnalytics.Params.PREFERENCE_KEY],
        )
    }

    @Test
    fun `displayThemeModeEvent formats theme switch event`() {
        val event = displayThemeModeEvent(mode = "dark")

        assertEquals(expected = SettingsAnalytics.Events.THEME_SWITCH, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = DISPLAY_SETTINGS_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = "dark"),
            actual = event.params[SettingsAnalytics.Params.THEME_MODE],
        )
    }
}
