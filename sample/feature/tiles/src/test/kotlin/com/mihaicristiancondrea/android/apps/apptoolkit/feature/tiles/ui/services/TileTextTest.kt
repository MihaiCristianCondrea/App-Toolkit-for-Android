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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TileTextTest {

    @Test
    fun `idle tile shows its title and summary`() {
        val text = TileText(title = "Coin Flip", subtitle = "Flip heads or tails instantly")

        assertEquals(
            VisibleTileText(label = "Coin Flip", subtitle = "Flip heads or tails instantly"),
            text.forSubtitleSupport(supportsSubtitle = true),
        )
        assertEquals(
            VisibleTileText(label = "Coin Flip", subtitle = null),
            text.forSubtitleSupport(supportsSubtitle = false),
        )
    }

    @Test
    fun `result replaces the subtitle where subtitles exist`() {
        val text = TileText(title = "Dice Roll", subtitle = "Roll a six-sided die", result = "Rolled 4")

        assertEquals(
            VisibleTileText(label = "Dice Roll", subtitle = "Rolled 4"),
            text.forSubtitleSupport(supportsSubtitle = true),
        )
    }

    @Test
    fun `result replaces the label before Android 10 so a tap stays visible`() {
        val text = TileText(title = "Coin Flip", subtitle = "Flip heads or tails instantly", result = "Heads")

        assertEquals(
            VisibleTileText(label = "Heads", subtitle = null),
            text.forSubtitleSupport(supportsSubtitle = false),
        )
    }
}
