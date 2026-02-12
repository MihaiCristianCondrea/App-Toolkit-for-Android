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

package com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme

/**
 * Palette variant indices for dynamic color schemes.
 */
object DynamicPaletteVariant {
    const val MIN: Int = 0
    const val MAX: Int = 7
    val indices: IntRange = MIN..MAX

    fun clamp(value: Int): Int = value.coerceIn(MIN, MAX)
}

/**
 * Supported static palette identifiers.
 */
object StaticPaletteIds {
    const val MONOCHROME = "monochrome"
    const val GOOGLE_BLUE = "blue"
    const val ANDROID = "android"
    const val GREEN = "green"
    const val RED = "red"
    const val YELLOW = "yellow"

    const val ROSE = "rose"
    const val CHRISTMAS = "christmas"
    const val HALLOWEEN = "halloween"
    const val SKIN = "skin"

    const val DEFAULT = "default"

    private val supportedOrder = listOf(
        MONOCHROME,
        GOOGLE_BLUE,
        ANDROID,
        GREEN,
        RED,
        YELLOW,
        ROSE,
        CHRISTMAS,
        HALLOWEEN,
        SKIN,
    )

    val withDefault: List<String> = listOf(DEFAULT) + supportedOrder

    fun sanitize(id: String): String = when (id) {
        DEFAULT -> DEFAULT
        in supportedOrder -> id
        else -> DEFAULT
    }
}
