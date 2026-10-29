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

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/** The brand SVG used by the marketing UI, exported from marketing/assets/shapes. */
internal object Cookie8Shape : Shape {
    private val source = PathParser().parsePathString("M127,4c11-5.4,23.9-5.4,34.9,0l33.9,16.6l35.7,12.2c11.6,4,20.7,13.1,24.7,24.7l12.2,35.7L285,127 c5.4,11,5.4,23.9,0,34.9l-16.6,33.9l-12.2,35.7c-4,11.6-13.1,20.7-24.7,24.7l-35.7,12.2L162,285c-11,5.4-23.9,5.4-34.9,0l-33.9-16.6 l-35.7-12.2c-11.6-4-20.7-13.1-24.7-24.7l-12.2-35.7L4,162c-5.4-11-5.4-23.9,0-34.9l16.6-33.9l12.2-35.7c4-11.6,13.1-20.7,24.7-24.7 l35.7-12.2L127,4z").toPath()
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply { addPath(source) }
        path.transform(Matrix().apply { scale(size.width / 289f, size.height / 289f) })
        return Outline.Generic(path)
    }
}
