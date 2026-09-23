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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WavyLineGeometryTest {

    @Test
    fun `the default size reproduces il_wavy_line`() {
        // The drawable's own viewport: 8 units tall, a 1-unit stroke, 15.2-unit half-waves.
        val geometry = wavyLineGeometry(length = 153f, breadth = 8f, strokeWidth = 1f)

        assertEquals(4f, geometry.centre)
        assertEquals(4.667f, geometry.controlOffset, absoluteTolerance = 0.001f)
        assertEquals(10, geometry.halfWaveCount)
        assertEquals(15.2f, geometry.halfWaveLength, absoluteTolerance = 0.001f)
    }

    @ParameterizedTest
    @ValueSource(floats = [37f, 100f, 211.5f, 360f, 1_080f])
    fun `any length is filled by whole half-waves that end inside the bounds`(length: Float) {
        val strokeWidth = 1f
        val geometry = wavyLineGeometry(length = length, breadth = 8f, strokeWidth = strokeWidth)

        val end: Float = geometry.start + geometry.halfWaveCount * geometry.halfWaveLength
        assertEquals(strokeWidth / 2f, geometry.start)
        assertEquals(length - strokeWidth / 2f, end, absoluteTolerance = 0.01f)
        // Stretched or squeezed only a little from the drawable's 15.2, never distorted.
        val natural = 15.2f
        assertTrue(abs(geometry.halfWaveLength - natural) <= natural / 2f)
    }

    @Test
    fun `the wave scales with its band so it keeps its shape`() {
        val small = wavyLineGeometry(length = 1_000f, breadth = 8f, strokeWidth = 1f)
        val large = wavyLineGeometry(length = 1_000f, breadth = 24f, strokeWidth = 3f)

        assertEquals(small.controlOffset * 3f, large.controlOffset, absoluteTolerance = 0.01f)
        assertEquals(
            small.halfWaveLength * 3f,
            large.halfWaveLength,
            absoluteTolerance = small.halfWaveLength,
        )
    }

    @Test
    fun `the crest and half the stroke fill the band exactly`() {
        val geometry = wavyLineGeometry(length = 500f, breadth = 12f, strokeWidth = 2f)

        val crest: Float = geometry.controlOffset * 0.75f
        assertEquals(6f, crest + 1f, absoluteTolerance = 0.001f)
    }

    @Test
    fun `a line too short for a full half-wave still draws one`() {
        val geometry = wavyLineGeometry(length = 4f, breadth = 8f, strokeWidth = 1f)

        assertEquals(1, geometry.halfWaveCount)
        assertEquals(3f, geometry.halfWaveLength)
    }

    @Test
    fun `a band no wider than the stroke draws a straight line`() {
        val geometry = wavyLineGeometry(length = 100f, breadth = 1f, strokeWidth = 1f)

        assertEquals(0f, geometry.controlOffset)
    }
}
