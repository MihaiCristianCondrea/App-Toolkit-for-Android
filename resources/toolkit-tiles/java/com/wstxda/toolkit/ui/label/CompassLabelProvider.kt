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

package com.wstxda.toolkit.ui.label

import android.content.Context
import com.wstxda.toolkit.R
import kotlin.math.roundToInt

class CompassLabelProvider(private val context: Context) {

    fun getLabel(isActive: Boolean, degrees: Float): CharSequence {
        return if (isActive) {
            val direction = when (degrees) {
                in 0.0..22.5, in 337.5..360.0 -> context.getString(R.string.N)
                in 22.5..67.5 -> context.getString(R.string.NE)
                in 67.5..112.5 -> context.getString(R.string.E)
                in 112.5..157.5 -> context.getString(R.string.SE)
                in 157.5..202.5 -> context.getString(R.string.S)
                in 202.5..247.5 -> context.getString(R.string.SW)
                in 247.5..292.5 -> context.getString(R.string.W)
                in 292.5..337.5 -> context.getString(R.string.NW)
                else -> ""
            }
            val degreesRounded = degrees.roundToInt() % 360
            context.getString(R.string.compass_tile_degrees, degreesRounded, direction)
        } else {
            context.getString(R.string.compass_tile)
        }
    }

    fun getSubtitle(isActive: Boolean): CharSequence {
        return if (isActive) {
            context.getString(R.string.tile_on)
        } else {
            context.getString(R.string.tile_off)
        }
    }
}