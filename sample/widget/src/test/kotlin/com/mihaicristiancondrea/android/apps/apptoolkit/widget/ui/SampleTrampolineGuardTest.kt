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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SampleTrampolineGuardTest {
    @Test
    fun `only missing Glance envelope failures are recoverable`() {
        for (message in listOf(
            "List adapter activity trampoline invoked without specifying target intent.",
            "List adapter activity trampoline invoked without trampoline type",
        )) {
            val failure = IllegalArgumentException(message)
            assertTrue(failure.isMissingGlanceAction(missingEnvelope = true))
            assertFalse(failure.isMissingGlanceAction(missingEnvelope = false))
        }
        assertFalse(IllegalArgumentException("another error").isMissingGlanceAction(missingEnvelope = true))
    }
}
