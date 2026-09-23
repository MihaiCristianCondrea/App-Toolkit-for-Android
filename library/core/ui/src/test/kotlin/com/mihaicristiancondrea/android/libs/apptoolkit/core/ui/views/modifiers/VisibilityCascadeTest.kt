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

class VisibilityCascadeTest {

    private var now: Long = 1_000L
    private val cascade = VisibilityCascade(clock = { now })

    @Test
    fun `the first screenful cascades from the top, as the index-based stagger did`() {
        val delays = (0 until 5).map { index -> delayFor(index = index) }

        assertEquals(listOf(0L, 64L, 128L, 192L, 256L), delays)
    }

    @Test
    fun `without an index elements cascade in the order they appear`() {
        val delays = List(size = 4) { delayFor(index = null) }

        assertEquals(listOf(0L, 64L, 128L, 192L), delays)
    }

    @Test
    fun `a row scrolled into view deep in a list starts at once`() {
        (0 until 20).forEach { index -> delayFor(index = index) }

        now += 400L
        val delays = listOf(80, 81).map { index -> delayFor(index = index) }

        assertEquals(listOf(0L, 64L), delays)
    }

    @Test
    fun `a wave scrolled in from the top cascades away from where it starts`() {
        now += 400L
        val delays = listOf(39, 38, 37).map { index -> delayFor(index = index) }

        assertEquals(listOf(0L, 64L, 128L), delays)
    }

    @Test
    fun `elements a frame or two apart join the same wave`() {
        val first = delayFor(index = null)
        now += 16L
        val second = delayFor(index = null)
        now += 16L
        val third = delayFor(index = null)

        assertEquals(listOf(0L, 64L, 128L), listOf(first, second, third))
    }

    @Test
    fun `elements past the cap wait no longer than the cap`() {
        val delays = (0 until 6).map { index -> delayFor(index = index, maxStaggeredItems = 3) }

        assertEquals(listOf(0L, 64L, 128L, 192L, 192L, 192L), delays)
    }

    private fun delayFor(index: Int?, maxStaggeredItems: Int = 20): Long =
        cascade.delayMillisFor(
            index = index,
            staggerDelayMillis = 64L,
            maxStaggeredItems = maxStaggeredItems,
        )
}
