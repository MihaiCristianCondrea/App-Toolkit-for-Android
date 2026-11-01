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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase

import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShowcaseCardWidthTest {

    @Test
    fun `a phone shows most of a card so the next one peeks in`() {
        assertEquals(353.46.dp.value, showcaseCardWidth(411.dp, 12.dp).value, 0.01f)
    }

    @Test
    fun `a phone card never grows past 360 dp`() {
        assertEquals(360.dp, showcaseCardWidth(599.dp, 12.dp))
    }

    @Test
    fun `a wide row fits whole cards of at least 260 dp`() {
        val width = showcaseCardWidth(840.dp, 12.dp)
        val columns = 3
        assertEquals((840.dp - 12.dp * (columns - 1)) / columns, width)
        assertTrue(width >= 260.dp)
    }
}
