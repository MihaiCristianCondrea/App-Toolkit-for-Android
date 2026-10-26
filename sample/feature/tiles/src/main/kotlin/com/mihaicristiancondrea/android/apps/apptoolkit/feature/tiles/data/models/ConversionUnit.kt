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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models

import java.math.BigDecimal

/** SI factors and offsets; Fahrenheit uses an exact 5/9 conversion in the repository. Data units use powers of 1000. */
enum class ConversionUnit(
    val category: ConversionCategory,
    val symbol: String,
    scale: String,
    offset: String = "0",
) {
    Millimetre(ConversionCategory.Length, "mm", "0.001"),
    Centimetre(ConversionCategory.Length, "cm", "0.01"),
    Metre(ConversionCategory.Length, "m", "1"),
    Kilometre(ConversionCategory.Length, "km", "1000"),
    Inch(ConversionCategory.Length, "in", "0.0254"),
    Foot(ConversionCategory.Length, "ft", "0.3048"),
    Yard(ConversionCategory.Length, "yd", "0.9144"),
    Mile(ConversionCategory.Length, "mi", "1609.344"),
    Gram(ConversionCategory.Mass, "g", "0.001"),
    Kilogram(ConversionCategory.Mass, "kg", "1"),
    Ounce(ConversionCategory.Mass, "oz", "0.028349523125"),
    Pound(ConversionCategory.Mass, "lb", "0.45359237"),
    Stone(ConversionCategory.Mass, "st", "6.35029318"),
    Celsius(ConversionCategory.Temperature, "°C", "1", "273.15"),
    Fahrenheit(ConversionCategory.Temperature, "°F", "1"),
    Kelvin(ConversionCategory.Temperature, "K", "1"),
    Millilitre(ConversionCategory.Volume, "mL", "0.001"),
    Litre(ConversionCategory.Volume, "L", "1"),
    CubicMetre(ConversionCategory.Volume, "m³", "1000"),
    UsGallon(ConversionCategory.Volume, "US gal", "3.785411784"),
    Byte(ConversionCategory.Data, "B", "1"),
    Kilobyte(ConversionCategory.Data, "kB", "1000"),
    Megabyte(ConversionCategory.Data, "MB", "1000000"),
    Gigabyte(ConversionCategory.Data, "GB", "1000000000"),
    Terabyte(ConversionCategory.Data, "TB", "1000000000000");

    val factor: BigDecimal = BigDecimal(scale)
    val baseOffset: BigDecimal = BigDecimal(offset)
}
