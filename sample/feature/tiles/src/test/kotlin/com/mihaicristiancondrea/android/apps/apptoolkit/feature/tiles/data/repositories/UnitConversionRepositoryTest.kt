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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UnitConversionRepositoryTest {
    private val repository = UnitConversionRepository()

    @Test
    fun `localized digits and decimal marks produce the same measurement`() {
        val inputs = listOf(
            "12.5",
            "12,5",
            "١٢٫٥",
            "۱۲٫۵",
            "१२.५",
            "১২.৫",
            "１２.５",
        )
        for (input in inputs) {
            assertEquals("1250", repository.convert(input, ConversionUnit.Metre, ConversionUnit.Centimetre), input)
        }
    }

    @Test
    fun `localized negative temperatures retain their sign`() {
        assertEquals("14", repository.convert("−١٠٫٠", ConversionUnit.Celsius, ConversionUnit.Fahrenheit))
        assertNull(repository.convert("−٣٠٠", ConversionUnit.Celsius, ConversionUnit.Kelvin))
    }

    @Test
    fun `grouped and mixed separator input is rejected instead of changing its value`() {
        val inputs = listOf(
            "١٬٢٣٤",
            "1,234.5",
            "1.234,5",
            "1 234",
            "1,234,567",
            "١٢٫٥٫٠",
        )
        for (input in inputs) {
            assertNull(repository.convert(input, ConversionUnit.Metre, ConversionUnit.Centimetre), input)
        }
    }
}
