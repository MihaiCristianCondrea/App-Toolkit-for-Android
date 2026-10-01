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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.rainfall

import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RainfallSimulationTest {

    private val phoneWidthPx = 1080
    private val phoneHeightPx = 2340
    private val pxPerDp = 2.625f

    @Test
    fun `drop count scales with density and is capped`() {
        val none = simulation(RainfallStyle(density = 0f))
        val light = simulation(RainfallStyle(density = 0.2f))
        val heavy = simulation(RainfallStyle(density = 0.8f))
        val capped = simulation(RainfallStyle(density = 1f, maxDrops = 25))

        assertEquals(0, none.dropCount)
        assertTrue(light.dropCount in 1 until heavy.dropCount)
        assertEquals(25, capped.dropCount)
    }

    @Test
    fun `the same physical screen gets the same amount of rain at any pixel density`() {
        val mdpi = RainfallSimulation(RainfallStyle(), Random(1)).apply {
            resize(widthPx = 400, heightPx = 800, pxPerDp = 1f)
        }
        val xxhdpi = RainfallSimulation(RainfallStyle(), Random(1)).apply {
            resize(widthPx = 1200, heightPx = 2400, pxPerDp = 3f)
        }

        assertEquals(mdpi.dropCount, xxhdpi.dropCount)
    }

    @Test
    fun `drops fall, stay on the surface sideways, and come back above it`() {
        val simulation = simulation(RainfallStyle(density = 0.5f, wind = 1f))
        val before = (0 until simulation.dropCount).map { simulation.positionOf(it).y }

        simulation.advance(elapsedMillis = 16f)

        (0 until simulation.dropCount).forEach { index ->
            val position = simulation.positionOf(index)
            assertTrue(position.y > before[index] || position.y < 0f)
            assertTrue(position.x in 0f..phoneWidthPx.toFloat())
        }
    }

    @Test
    fun `landing drops splash, unless splashes are off`() {
        val splashing = simulation(RainfallStyle(density = 0.5f))
        val dry = simulation(RainfallStyle(density = 0.5f, splashes = false))

        repeat(times = 120) {
            splashing.advance(elapsedMillis = 16f)
            dry.advance(elapsedMillis = 16f)
        }

        assertTrue(splashing.splashCount > 0)
        assertEquals(0, dry.splashCount)
    }

    @Test
    fun `gusts swing the wind and steady wind stays put`() {
        val gusty = simulation(RainfallStyle(gusts = 1f))
        val steady = simulation(RainfallStyle(gusts = 0f))
        val gustySlants = mutableSetOf<Float>()
        val steadySlants = mutableSetOf<Float>()

        repeat(times = 200) {
            gusty.advance(elapsedMillis = 50f)
            steady.advance(elapsedMillis = 50f)
            gustySlants += gusty.windSlant
            steadySlants += steady.windSlant
        }

        assertTrue(gustySlants.max() - gustySlants.min() > 0.2f)
        assertEquals(1, steadySlants.size)
    }

    @Test
    fun `showers thin the rain and steady rain keeps every drop`() {
        val showery = simulation(RainfallStyle(showers = 1f))
        val steady = simulation(RainfallStyle(showers = 0f))
        var thinnest = 1f

        repeat(times = 600) {
            showery.advance(elapsedMillis = 50f)
            steady.advance(elapsedMillis = 50f)
            thinnest = minOf(thinnest, showery.intensity)
            assertEquals(1f, steady.intensity)
        }

        assertTrue(thinnest < 0.5f)
        assertFalse(showery.intensity > 1f)
    }

    @Test
    fun `the wind stays smooth however long the rain falls`() {
        // No drops, so only the waves move: two hours of 50ms frames.
        val rain = simulation(RainfallStyle(density = 0f, gusts = 1f))
        var previous = rain.windSlant
        var largestStep = 0f

        repeat(times = 144_000) {
            rain.advance(elapsedMillis = 50f)
            largestStep = maxOf(largestStep, kotlin.math.abs(rain.windSlant - previous))
            previous = rain.windSlant
        }

        assertTrue(largestStep < 0.05f, "the wind jumped by $largestStep in one frame")
    }

    private fun simulation(style: RainfallStyle) = RainfallSimulation(style, Random(7)).apply {
        resize(widthPx = phoneWidthPx, heightPx = phoneHeightPx, pxPerDp = pxPerDp)
    }
}
