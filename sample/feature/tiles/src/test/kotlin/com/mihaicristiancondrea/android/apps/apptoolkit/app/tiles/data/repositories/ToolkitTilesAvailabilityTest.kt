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

/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileStatus
import kotlin.test.assertEquals
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class ToolkitTilesAvailabilityTest {

    @Test
    fun `unavailable torch removes every torch tool and empty categories`() {
        val categories = persistentListOf(
            category("system", tile("compass"), tile("flash_dimmer")),
            category("signals", tile("morse"), tile("sos")),
        )

        val result = filterUnavailableTorchTools(categories, isTorchAvailable = false)

        assertEquals(listOf("system"), result.map { it.id })
        assertEquals(listOf("compass"), result.single().tiles.map { it.id })
    }

    @Test
    fun `available torch keeps the complete catalogue`() {
        val categories = persistentListOf(
            category("signals", tile("flash_dimmer"), tile("morse"), tile("sos"))
        )

        val result = filterUnavailableTorchTools(categories, isTorchAvailable = true)

        assertEquals(categories, result)
    }

    private fun category(id: String, vararg tiles: ToolkitTileData) = ToolkitTileCategoryData(
        id = id,
        tiles = persistentListOf(*tiles),
    )

    private fun tile(id: String) = ToolkitTileData(
        id = id,
        status = ToolkitTileStatus.Available,
    )
}

