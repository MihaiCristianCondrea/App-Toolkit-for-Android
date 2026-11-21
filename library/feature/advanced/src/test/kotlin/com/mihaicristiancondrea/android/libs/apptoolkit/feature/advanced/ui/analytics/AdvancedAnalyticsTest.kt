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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AdvancedAnalyticsTest {

    @Test
    fun `advancedPreferenceTapEvent reports the advanced screen and preference key`() {
        val event = advancedPreferenceTapEvent(preferenceKey = AdvancedPreferenceKeys.CLEAR_CACHE)

        assertEquals(SettingsAnalytics.Events.PREFERENCE_VIEW, event.name)
        assertEquals(
            mapOf(
                SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = ADVANCED_SETTINGS_SCREEN.name),
                SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = AdvancedPreferenceKeys.CLEAR_CACHE),
            ),
            event.params,
        )
    }
}
