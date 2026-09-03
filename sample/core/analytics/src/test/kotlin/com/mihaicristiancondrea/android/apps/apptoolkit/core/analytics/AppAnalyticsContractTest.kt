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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppAnalyticsContractTest {

    @Test
    fun `registered event names conform to GA4 naming rules`() {
        AppGa4Contract.allEventNames().forEach { eventName ->
            assertTrue(AppGa4ContractValidator.isValidEventName(eventName), eventName)
        }
    }

    @Test
    fun `screen names and classes are unique and nonblank`() {
        val screens = AppScreenTracking.Screens.all
        val names = screens.map(TrackedScreen::name)
        val classes = screens.map(TrackedScreen::className)

        assertEquals(names.size, names.toSet().size)
        assertEquals(classes.size, classes.toSet().size)
        assertTrue(names.none(String::isBlank))
        assertTrue(classes.none(String::isBlank))
    }

    @Test
    fun `registered event schemas contain no forbidden parameters`() {
        AppGa4Contract.allEventNames().forEach { eventName ->
            assertTrue(
                AppGa4ContractValidator.forbiddenParams(
                    AppGa4Contract.requiredParams(eventName),
                ).isEmpty(),
                eventName,
            )
        }
    }
}
