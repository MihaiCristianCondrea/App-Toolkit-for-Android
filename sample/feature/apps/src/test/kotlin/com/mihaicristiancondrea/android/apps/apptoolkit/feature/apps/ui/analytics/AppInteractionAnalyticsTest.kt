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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppInteractionAnalyticsTest {
    @Test fun `app interaction keeps its event schema`() {
        val telemetry = FakeTelemetryRepository()
        telemetry.logAppInteraction("more_apps", AppSummary("App", "example.app", ""), AppInteractionType.AddFavorite)
        val event = telemetry.loggedEvents.single()
        assertEquals("app_card_interaction", event.name)
        assertEquals(setOf("source", "interaction", "package_name", "app_name"), event.params.keys)
        assertTrue(event.params.keys.containsAll(AppInteractionGa4Contract.requiredParams))
        assertTrue(AppGa4ContractValidator.isValidEventName(event.name))
        assertTrue(AppGa4ContractValidator.forbiddenParams(event.params.keys).isEmpty())
    }
}
