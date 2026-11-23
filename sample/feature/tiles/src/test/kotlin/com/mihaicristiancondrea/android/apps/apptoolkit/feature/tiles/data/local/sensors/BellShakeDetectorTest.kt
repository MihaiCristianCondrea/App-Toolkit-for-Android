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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BellShakeDetectorTest {
    @Test
    fun `weak motion and sustained acceleration do not repeat a strike`() {
        val detector = BellShakeDetector()
        assertNull(detector.strikeStrength(4f, 1f, 1f, 0L))
        assertNotNull(detector.strikeStrength(10f, 1f, 0f, 100_000_000L))
        assertNull(detector.strikeStrength(11f, 1f, 0f, 220_000_000L))
    }

    @Test
    fun `a shake reversal can strike after debounce and stronger shakes ring louder`() {
        val detector = BellShakeDetector()
        val soft = assertNotNull(detector.strikeStrength(9f, 0f, 0f, 0L))
        assertNull(detector.strikeStrength(-14f, 0f, 0f, 40_000_000L))
        val hard = assertNotNull(detector.strikeStrength(-14f, 0f, 0f, 110_000_000L))
        assertTrue(hard > soft)
    }

    @Test
    fun `returning to rest rearms a same direction shake`() {
        val detector = BellShakeDetector()
        assertNotNull(detector.strikeStrength(12f, 0f, 0f, 0L))
        assertNull(detector.strikeStrength(0f, 0f, 0f, 50_000_000L))
        assertNotNull(detector.strikeStrength(12f, 0f, 0f, 100_000_000L))
    }
}
