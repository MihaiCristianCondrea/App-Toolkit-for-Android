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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions

import java.math.BigDecimal

/** Decimal input has no grouping; comma, dot and Arabic decimal marks mean a fraction. */
internal fun String.toToolDecimalOrNull(): BigDecimal? {
    val number = normalizedToolNumber() ?: return null
    if (!DECIMAL.matches(number)) return null
    return number.toBigDecimalOrNull()
}

internal fun String.toToolLongOrNull(): Long? = normalizedToolNumber()?.toLongOrNull()

private fun String.normalizedToolNumber(): String? {
    val input = trim()
    if (input.length > 64) return null
    return buildString {
        for (character in input) {
            when {
                character == ',' || character == '\u066b' -> append('.')
                character == '\u2212' -> append('-')
                Character.isDigit(character) -> append(Character.digit(character, 10))
                else -> append(character)
            }
        }
    }
}

private val DECIMAL = Regex("[-+]?(?:[0-9]+(?:[.][0-9]*)?|[.][0-9]+)")
