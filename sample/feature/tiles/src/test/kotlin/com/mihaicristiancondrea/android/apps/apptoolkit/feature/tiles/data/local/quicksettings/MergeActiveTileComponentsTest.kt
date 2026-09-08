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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Covers [mergeActiveTileComponents].
 *
 * `sysui_qs_tiles` lags behind `requestAddTileService`, so reading it alone left a tile the user had
 * just added still reported as missing, and the Quick Tools list did not react.
 */
class MergeActiveTileComponentsTest {

    @Test
    fun `a tile this app recorded counts as added while the system list still lags`() {
        val active = mergeActiveTileComponents(
            systemTiles = setOf(COIN),
            recorded = mapOf(DICE to true),
        )

        assertEquals(setOf(COIN, DICE), active)
    }

    @Test
    fun `a tile this app recorded as removed drops out of the system list`() {
        val active = mergeActiveTileComponents(
            systemTiles = setOf(COIN, DICE),
            recorded = mapOf(DICE to false),
        )

        assertEquals(setOf(COIN), active)
    }

    @Test
    fun `an unreadable system list falls back to what this app recorded`() {
        val active = mergeActiveTileComponents(
            systemTiles = null,
            recorded = mapOf(COIN to true, DICE to false),
        )

        assertEquals(setOf(COIN), active)
    }

    @Test
    fun `components the app never recorded follow the system list`() {
        val active = mergeActiveTileComponents(systemTiles = setOf(COIN), recorded = emptyMap())

        assertEquals(setOf(COIN), active)
    }

    @Test
    fun `entries that are not booleans are ignored rather than trusted`() {
        val active = mergeActiveTileComponents(
            systemTiles = emptySet(),
            recorded = mapOf(COIN to "true", DICE to true),
        )

        assertEquals(setOf(DICE), active)
    }

    private companion object {
        const val COIN: String = "com.example/com.example.CoinTileService"
        const val DICE: String = "com.example/com.example.DiceTileService"
    }
}
