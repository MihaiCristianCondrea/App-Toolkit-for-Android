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
import kotlin.math.PI
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * The crash in `docs/crashes/fixed/compass-azimuth-nan-exception`: a NaN azimuth reached
 * `roundToInt`, which throws.
 */
class SensorReadingsTest {

    @Test
    fun `a nan or infinite azimuth gives no heading instead of throwing`() {
        assertNull(azimuthDegrees(Float.NaN))
        assertNull(azimuthDegrees(Float.POSITIVE_INFINITY))
        assertNull(azimuthDegrees(Float.NEGATIVE_INFINITY))
    }

    @Test
    fun `headings are whole degrees from 0 to 359`() {
        assertEquals(0f, azimuthDegrees(0f))
        assertEquals(90f, azimuthDegrees((PI / 2).toFloat()))
        assertEquals(270f, azimuthDegrees((-PI / 2).toFloat()))
        assertEquals(180f, azimuthDegrees(PI.toFloat()))
        assertEquals(0f, azimuthDegrees((2 * PI).toFloat()))
    }

    @Test
    fun `a tilt with a nan angle is dropped`() {
        assertNull(tiltDegrees(Float.NaN, 0f))
        assertNull(tiltDegrees(0f, Float.NaN))
        assertEquals(90f to 0f, tiltDegrees((PI / 2).toFloat(), 0f))
    }

    @Test
    fun `a reading with any non finite value is not a reading`() {
        assertTrue(floatArrayOf(0.1f, -9.8f, 0f).isFiniteReading())
        assertFalse(floatArrayOf(0.1f, Float.NaN, 0f).isFiniteReading())
        assertFalse(floatArrayOf(Float.POSITIVE_INFINITY, 0f, 0f).isFiniteReading())
    }
}
