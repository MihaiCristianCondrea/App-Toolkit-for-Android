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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ToolkitTilesSearchTest {

    // String resources stand in as ids; the test resolves them from this table.
    private val text = mapOf(
        1 to "Sensors", 2 to "Level", 3 to "Check a surface is flat", 4 to "Compass", 5 to "Find north",
        6 to "Games", 7 to "Coin flip", 8 to "Heads or tails",
    )

    private fun tile(id: String, title: Int, summary: Int) =
        ToolkitTile(id, title, summary, ToolkitTileIcon.Level, ToolkitTileStatus.Added)

    private val catalog = listOf(
        ToolkitTileCategory("sensors", 1, ToolkitTileIcon.Level, persistentListOf(tile("level", 2, 3), tile("compass", 4, 5))),
        ToolkitTileCategory("games", 6, ToolkitTileIcon.Coin, persistentListOf(tile("coin", 7, 8))),
    )

    private fun search(query: String) = catalog.search(query) { id -> text.getValue(id) }

    @Test
    fun `a blank query keeps the whole catalog`() {
        assertEquals(catalog, search(""))
    }

    @Test
    fun `a tile matches by title or summary and keeps only matching tiles`() {
        assertEquals(listOf("sensors" to listOf("compass")), search("north").map { it.id to it.tiles.map { tile -> tile.id } })
        assertEquals(listOf("games" to listOf("coin")), search("COIN").map { it.id to it.tiles.map { tile -> tile.id } })
    }

    @Test
    fun `a category matching by name keeps all its tiles`() {
        assertEquals(listOf("sensors" to listOf("level", "compass")), search("sensor").map { it.id to it.tiles.map { tile -> tile.id } })
    }

    @Test
    fun `nothing matching leaves nothing`() {
        assertEquals(emptyList(), search("camera"))
    }
}
