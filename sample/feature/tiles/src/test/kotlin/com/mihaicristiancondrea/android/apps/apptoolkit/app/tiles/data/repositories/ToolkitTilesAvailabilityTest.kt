/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.ToolkitTileStatus
import kotlin.test.assertEquals
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class ToolkitTilesAvailabilityTest {

    @Test
    fun `unavailable torch removes every torch tool and empty categories`() {
        val categories = persistentListOf(
            category("system", tile("battery"), tile("flash_dimmer")),
            category("signals", tile("morse"), tile("sos")),
        )

        val result = filterUnavailableTorchTools(categories, isTorchAvailable = false)

        assertEquals(listOf("system"), result.map { it.id })
        assertEquals(listOf("battery"), result.single().tiles.map { it.id })
    }

    @Test
    fun `available torch keeps the complete catalogue`() {
        val categories = persistentListOf(
            category("signals", tile("flash_dimmer"), tile("morse"), tile("sos"))
        )

        val result = filterUnavailableTorchTools(categories, isTorchAvailable = true)

        assertEquals(categories, result)
    }

    private fun category(id: String, vararg tiles: ToolkitTile) = ToolkitTileCategory(
        id = id,
        titleResId = 0,
        icon = ToolkitTileIcon.Battery,
        tiles = persistentListOf(*tiles),
    )

    private fun tile(id: String) = ToolkitTile(
        id = id,
        titleResId = 0,
        summaryResId = 0,
        icon = ToolkitTileIcon.Battery,
        status = ToolkitTileStatus.Available,
    )
}

