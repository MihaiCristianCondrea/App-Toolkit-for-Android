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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.snowfall

import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SnowfallSimulationTest {

    private val phoneWidthPx = 1080
    private val phoneHeightPx = 2340
    private val pxPerDp = 2.625f

    @Test
    fun `flake count scales with density and is capped`() {
        val none = simulation(SnowfallStyle(density = 0f))
        val light = simulation(SnowfallStyle(density = 0.2f))
        val heavy = simulation(SnowfallStyle(density = 0.8f))
        val capped = simulation(SnowfallStyle(density = 1f, maxFlakes = 25))

        assertEquals(0, none.flakeCount)
        assertTrue(light.flakeCount in 1 until heavy.flakeCount)
        assertEquals(25, capped.flakeCount)
    }

    @Test
    fun `the same physical screen gets the same amount of snow at any pixel density`() {
        val mdpi = SnowfallSimulation(SnowfallStyle(), Random(1)).apply {
            resize(widthPx = 400, heightPx = 800, pxPerDp = 1f)
        }
        val xxhdpi = SnowfallSimulation(SnowfallStyle(), Random(1)).apply {
            resize(widthPx = 1200, heightPx = 2400, pxPerDp = 3f)
        }

        assertEquals(mdpi.flakeCount, xxhdpi.flakeCount)
    }

    @Test
    fun `flakes fall and re-enter from the top once past the bottom`() {
        val simulation = simulation(SnowfallStyle(density = 0.5f))
        val before = (0 until simulation.flakeCount).map { simulation.positionOf(it).y }

        simulation.advance(elapsedMillis = 16f)
        val after = (0 until simulation.flakeCount).map { simulation.positionOf(it).y }
        assertTrue(before.indices.all { after[it] > before[it] }, "every flake moves down")

        // Long enough for every flake to cross the whole screen at least once.
        repeat(3_000) { simulation.advance(elapsedMillis = 16f) }
        (0 until simulation.flakeCount).forEach { index ->
            val position = simulation.positionOf(index)
            val radius = simulation.radiusOf(index)
            assertTrue(position.y in -radius..phoneHeightPx + radius, "flake $index left the screen")
        }
    }

    @Test
    fun `a late frame moves the snow by one bounded step`() {
        val slow = simulation(SnowfallStyle())
        val late = simulation(SnowfallStyle())

        slow.advance(elapsedMillis = 50f)
        late.advance(elapsedMillis = 5_000f)

        (0 until slow.flakeCount).forEach { index ->
            assertEquals(slow.positionOf(index).y, late.positionOf(index).y, 0.001f)
        }
    }

    @Test
    fun `wind pushes flakes sideways but keeps them on screen`() {
        val simulation = simulation(SnowfallStyle(wind = 1f, density = 0.5f))

        repeat(3_000) { simulation.advance(elapsedMillis = 16f) }

        (0 until simulation.flakeCount).forEach { index ->
            val x = simulation.positionOf(index).x
            assertTrue(x in -200f..phoneWidthPx + 200f, "flake $index drifted away to $x")
        }
    }

    private fun simulation(style: SnowfallStyle): SnowfallSimulation =
        SnowfallSimulation(style = style, random = Random(seed = 42)).apply {
            resize(widthPx = phoneWidthPx, heightPx = phoneHeightPx, pxPerDp = pxPerDp)
        }
}
