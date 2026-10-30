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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

/**
 * The tool shapes exactly as the marketing kit draws them (`shapeUrl` in
 * marketing/ui/src/kit/assets.js): a polar curve sampled at 180 points, scaled so its widest point
 * touches 1% inside the box. The kit is the design, so these replace the `MaterialShapes` the app
 * used to cut its faces and badges from, whose bursts and cookies are shallower and differently
 * counted. Keep the formulas identical to the kit's when either changes.
 */
internal class KitShape private constructor(private val radius: (Double) -> Double) : Shape {
    // The outline on a unit box, computed once per shape.
    private val points: List<Offset> by lazy {
        val raw = List(SAMPLES) { index ->
            val t = index.toDouble() / SAMPLES * 2 * PI
            val r = radius(t)
            r * cos(t) to r * sin(t)
        }
        val reach = raw.maxOf { (x, y) -> max(abs(x), abs(y)) }
        raw.map { (x, y) -> Offset((0.5 + x / reach * 0.49).toFloat(), (0.5 + y / reach * 0.49).toFloat()) }
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Generic(
            Path().apply {
                points.forEachIndexed { index, point ->
                    val x = point.x * size.width
                    val y = point.y * size.height
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            },
        )

    companion object {
        private const val SAMPLES = 180

        private fun polygon(sides: Int, t: Double): Double {
            val segment = 2 * PI / sides
            val u = t + segment / 2
            val m = ((u % segment) + segment) % segment - segment / 2
            return cos(PI / sides) / cos(m)
        }

        private fun cookie(lobes: Int) = KitShape { t -> 0.9 + 0.1 * cos(lobes * t) }

        val Cookie7 = cookie(7)
        val Cookie9 = cookie(9)
        val Cookie12 = cookie(12)
        val Pill = KitShape { t -> 1 / (abs(cos(t)).pow(3.2) + abs(sin(t)).pow(3.2)).pow(1 / 3.2) }
        val Gem = KitShape { t -> 0.55 * polygon(6, t) + 0.4 }
        val Flower = KitShape { t -> 0.8 + 0.2 * cos(8 * t) }
        val SoftBurst = KitShape { t -> 0.86 + 0.14 * cos(10 * t) }
        val Burst = KitShape { t -> 0.74 + 0.26 * abs(cos(6 * t)).pow(0.7) }
        val Sunny = KitShape { t -> 0.82 + 0.18 * cos(12 * t) }
    }
}
