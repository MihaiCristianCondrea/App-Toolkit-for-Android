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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CloseGuardTest {

    @Test
    fun `a close requested while one plays is ignored`() = runTest {
        val guard = CloseGuard(this)
        var closes = 0

        guard.launch {
            delay(200)
            closes++
        }
        runCurrent()
        assertTrue(guard.isRunning)

        advanceTimeBy(100)
        guard.launch { closes++ }
        advanceTimeBy(200)
        runCurrent()

        assertEquals(1, closes)
        assertFalse(guard.isRunning)
    }

    @Test
    fun `a close can start again once the last one finished`() = runTest {
        val guard = CloseGuard(this)
        var closes = 0

        guard.launch { closes++ }
        runCurrent()
        guard.launch { closes++ }
        runCurrent()

        assertEquals(2, closes)
    }

    @Test
    fun `cancelling a running close prevents its pop and allows another close`() = runTest {
        val guard = CloseGuard(this)
        var closes = 0

        guard.launch {
            delay(200)
            closes++
        }
        runCurrent()
        assertTrue(guard.isRunning)

        guard.cancel()
        assertFalse(guard.isRunning)

        advanceTimeBy(300)
        runCurrent()
        assertEquals(0, closes)

        guard.launch { closes++ }
        runCurrent()
        assertEquals(1, closes)
    }
}
