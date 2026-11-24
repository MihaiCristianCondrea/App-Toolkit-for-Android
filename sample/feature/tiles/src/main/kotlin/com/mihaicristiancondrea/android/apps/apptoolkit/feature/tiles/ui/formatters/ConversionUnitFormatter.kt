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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.formatters

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit

@Composable
internal fun ConversionUnit.displayName(): String {
    val locale = LocalConfiguration.current.locales[0]
    val measureUnit = when (this) {
        ConversionUnit.Millimetre -> MeasureUnit.MILLIMETER
        ConversionUnit.Centimetre -> MeasureUnit.CENTIMETER
        ConversionUnit.Metre -> MeasureUnit.METER
        ConversionUnit.Kilometre -> MeasureUnit.KILOMETER
        ConversionUnit.Inch -> MeasureUnit.INCH
        ConversionUnit.Foot -> MeasureUnit.FOOT
        ConversionUnit.Yard -> MeasureUnit.YARD
        ConversionUnit.Mile -> MeasureUnit.MILE
        ConversionUnit.Gram -> MeasureUnit.GRAM
        ConversionUnit.Kilogram -> MeasureUnit.KILOGRAM
        ConversionUnit.Ounce -> MeasureUnit.OUNCE
        ConversionUnit.Pound -> MeasureUnit.POUND
        ConversionUnit.Stone -> MeasureUnit.STONE
        ConversionUnit.Celsius -> MeasureUnit.CELSIUS
        ConversionUnit.Fahrenheit -> MeasureUnit.FAHRENHEIT
        ConversionUnit.Kelvin -> MeasureUnit.KELVIN
        ConversionUnit.Millilitre -> MeasureUnit.MILLILITER
        ConversionUnit.Litre -> MeasureUnit.LITER
        ConversionUnit.CubicMetre -> MeasureUnit.CUBIC_METER
        ConversionUnit.UsGallon -> MeasureUnit.GALLON
        ConversionUnit.Byte -> MeasureUnit.BYTE
        ConversionUnit.Kilobyte -> MeasureUnit.KILOBYTE
        ConversionUnit.Megabyte -> MeasureUnit.MEGABYTE
        ConversionUnit.Gigabyte -> MeasureUnit.GIGABYTE
        ConversionUnit.Terabyte -> MeasureUnit.TERABYTE
    }
    return remember(locale, measureUnit) {
        val format = MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.WIDE)
        if (Build.VERSION.SDK_INT >= 28) format.getUnitDisplayName(measureUnit)
        else format.formatMeasures(Measure(1, measureUnit))
    }
}
