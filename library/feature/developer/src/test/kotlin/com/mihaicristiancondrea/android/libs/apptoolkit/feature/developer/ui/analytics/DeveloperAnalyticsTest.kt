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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeveloperAnalyticsTest {
    @Test
    fun `saved option uses bounded settings vocabulary without values or host details`() {
        val event = developerOptionSavedEvent(option = DeveloperOption.Layout)
        assertEquals(SettingsAnalytics.Events.ACTION, event.name)
        assertEquals(
            mapOf(
                SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = DEVELOPER_OPTIONS_SCREEN.name),
                SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(value = "save_developer_option"),
                SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = "layout"),
            ),
            event.params,
        )
    }

    @Test
    fun `reset uses the developer screen and shared settings action`() {
        val event = developerOptionsResetEvent()
        assertEquals(SettingsAnalytics.Events.ACTION, event.name)
        assertEquals(
            mapOf(
                SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = DEVELOPER_OPTIONS_SCREEN.name),
                SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(value = "reset_developer_options"),
            ),
            event.params,
        )
    }

    @Test
    fun `every developer option maps to a unique non-blank preference key`() {
        val keys = DeveloperOption.entries.map { option -> developerOptionKey(option = option) }
        assertTrue(keys.all { it.isNotBlank() })
        assertEquals(DeveloperOption.entries.size, keys.toSet().size)
    }
}
