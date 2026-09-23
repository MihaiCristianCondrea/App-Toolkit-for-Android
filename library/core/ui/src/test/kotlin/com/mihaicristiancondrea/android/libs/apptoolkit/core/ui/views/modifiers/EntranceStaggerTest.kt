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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EntranceStaggerTest {

    private var now: Long = 1_000L
    private val stagger = EntranceStagger(
        staggerDelayMillis = 64L,
        maxStaggeredItems = 3,
        clock = { now },
    )

    @Test
    fun `items of the first reveal come in one stagger apart, in arrival order`() {
        val delays = List(size = 3) { stagger.delayForNextItemMillis() }

        assertEquals(listOf(0L, 64L, 128L), delays)
    }

    @Test
    fun `items past the cap wait no longer than the cap`() {
        val delays = List(size = 6) { stagger.delayForNextItemMillis() }

        assertEquals(listOf(0L, 64L, 128L, 192L, 192L, 192L), delays)
    }

    @Test
    fun `an item scrolled in after the first reveal comes in at once`() {
        repeat(times = 10) { stagger.delayForNextItemMillis() }

        now += 1_000L

        assertEquals(0L, stagger.delayForNextItemMillis())
    }

    @Test
    fun `items arriving a frame apart still belong to the first reveal`() {
        val first = stagger.delayForNextItemMillis()
        now += 16L
        val second = stagger.delayForNextItemMillis()

        assertEquals(listOf(0L, 64L), listOf(first, second))
    }
}
