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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BillSplitterRepositoryTest {
    private val repository = BillSplitterRepository()

    @Test
    fun `tip rounds once and the exact total is split`() {
        val split = assertNotNull(repository.split("125.50", 15, "3"))
        assertEquals(BigDecimal("18.83"), split.tip)
        assertEquals(BigDecimal("144.33"), split.total)
        assertEquals(BigDecimal("48.11"), split.perPerson)
    }

    @Test
    fun `shares round up to cover the total including small and zero bills`() {
        assertEquals(BigDecimal("3.67"), repository.split("10", 10, "3")?.perPerson)
        assertEquals(BigDecimal("0.01"), repository.split("0.01", 20, "3")?.perPerson)
        assertEquals(BigDecimal("0.00"), repository.split("0", 15, "1")?.perPerson)
    }

    @Test
    fun `localized decimal input and people are accepted`() {
        assertEquals(BigDecimal("7.50"), repository.split("12,50", 20, "2")?.perPerson)
        assertEquals(BigDecimal("7.50"), repository.split("١٢٫٥٠", 20, "٢")?.perPerson)
    }

    @Test
    fun `invalid amounts people and tips do not produce results`() {
        for (bill in listOf("", "-1", "1.001", "1,000", "1.000", "NaN", "1e3", "1,000.00")) {
            assertNull(repository.split(bill, 15, "2"), bill)
        }
        for (people in listOf("", "0", "-1", "2.5", "9223372036854775808")) {
            assertNull(repository.split("100", 15, people), people)
        }
        assertNull(repository.split("100", -1, "2"))
        assertNull(repository.split("100", 101, "2"))
    }
}
