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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models

import kotlin.math.atan2
import kotlin.math.min

internal data class ProtractorGeometry(val x: Float, val y: Float, val radius: Float, val landscape: Boolean) {
    fun angleAt(x: Float, y: Float): Float {
        val angle = if (landscape) atan2(this.y - y, x - this.x) else atan2(x - this.x, this.y - y)
        return Math.toDegrees(angle.toDouble()).toFloat().coerceIn(0f, 180f)
    }
}
internal fun protractorGeometry(width: Float, height: Float): ProtractorGeometry {
    val landscape = width >= 650f || width > height
    val y = if (landscape) height else ((height - 204f) / 2f).coerceAtLeast(140f)
    val x = if (landscape) (width - 320f) / 2f else 0f
    val radius = (if (landscape) min((width - 368f) / 2f, height - 96f) else min(width - 24f, y - 96f)).coerceAtLeast(60f)
    return ProtractorGeometry(x, y, radius, landscape)
}
