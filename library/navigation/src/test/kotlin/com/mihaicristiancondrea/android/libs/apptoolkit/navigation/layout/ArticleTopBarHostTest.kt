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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ArticleTopBarHostTest {

    private val host = ArticleTopBarHost()
    private val owner = Any()

    private fun declare(
        compact: Boolean = false,
        progress: (() -> Float)? = null,
        title: String = "Title",
        owner: Any = this.owner,
    ) = host.set(owner, title, { compact }, progress, brand = null, brandContentDescription = null)

    @Test
    fun `nothing is declared until a screen declares it`() {
        assertFalse(host.isDeclared)
        assertFalse(host.isCompact)
        assertFalse(host.hasProgress)
        assertEquals(0f, host.progress())
        assertEquals("", host.title)
        assertNull(host.brand)
    }

    @Test
    fun `a declared article follows the screen's compact state`() {
        var compact = false
        host.set(owner, "Title", { compact }, progress = null, brand = null, brandContentDescription = null)

        assertTrue(host.isDeclared)
        assertFalse(host.isCompact)
        compact = true
        assertTrue(host.isCompact)
        assertEquals("Title", host.title)
    }

    @Test
    fun `an article without progress reports none`() {
        declare(compact = true)

        assertFalse(host.hasProgress)
        assertEquals(0f, host.progress())
    }

    @ParameterizedTest
    @CsvSource("0, 0", "1, 1", "0.4, 0.4", "-0.5, 0", "1.5, 1", "NaN, 0")
    fun `progress is clamped to the range from 0 to 1`(reported: Float, expected: Float) {
        declare(progress = { reported })

        assertTrue(host.hasProgress)
        assertEquals(expected, host.progress())
    }

    @Test
    fun `the brand and its description are kept as declared`() {
        val brand = ColorPainter(Color.Red)
        host.set(owner, "Title", { true }, progress = null, brand = brand, brandContentDescription = "Publisher")

        assertSame(brand, host.brand)
        assertEquals("Publisher", host.brandContentDescription)
    }

    @Test
    fun `clearing removes everything the screen declared`() {
        host.set(owner, "Title", { true }, { 0.5f }, ColorPainter(Color.Red), "Publisher")

        host.clear(owner)

        assertFalse(host.isDeclared)
        assertFalse(host.isCompact)
        assertFalse(host.hasProgress)
        assertEquals(0f, host.progress())
        assertEquals("", host.title)
        assertNull(host.brand)
        assertNull(host.brandContentDescription)
    }

    @Test
    fun `a screen that left cannot clear the one that declared after it`() {
        val next = Any()
        declare(title = "First")
        declare(title = "Second", compact = true, owner = next)

        host.clear(owner)

        assertTrue(host.isDeclared)
        assertTrue(host.isCompact)
        assertEquals("Second", host.title)
    }
}
