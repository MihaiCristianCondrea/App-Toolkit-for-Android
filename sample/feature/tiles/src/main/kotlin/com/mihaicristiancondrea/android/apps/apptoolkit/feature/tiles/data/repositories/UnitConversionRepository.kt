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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolDecimalOrNull
import java.math.BigDecimal
import java.math.MathContext

class UnitConversionRepository {
    /**
     * Accepts localized decimal digits and a dot, comma or Arabic decimal separator.
     * Grouping separators are unsupported. Returns null for invalid input or temperatures
     * below absolute zero.
     */
    fun convert(input: String, from: ConversionUnit, to: ConversionUnit): String? {
        require(from.category == to.category)
        val value = input.toToolDecimalOrNull() ?: return null
        val base = if (from == ConversionUnit.Fahrenheit) {
            value.subtract(BigDecimal("32")).multiply(BigDecimal("5"))
                .divide(BigDecimal("9"), MathContext.DECIMAL64).add(BigDecimal("273.15"))
        } else value.multiply(from.factor).add(from.baseOffset)
        if (from.category == ConversionCategory.Temperature && base.signum() < 0) return null
        val result = if (to == ConversionUnit.Fahrenheit) {
            base.subtract(BigDecimal("273.15")).multiply(BigDecimal("9"))
                .divide(BigDecimal("5"), MathContext.DECIMAL64).add(BigDecimal("32"))
        } else base.subtract(to.baseOffset).divide(to.factor, MathContext.DECIMAL64)
        return result.stripTrailingZeros().toPlainString()
    }
}
