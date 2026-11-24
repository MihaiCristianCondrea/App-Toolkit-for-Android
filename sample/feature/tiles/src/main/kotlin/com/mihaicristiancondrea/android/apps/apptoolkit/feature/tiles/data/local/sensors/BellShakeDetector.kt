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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors

import kotlin.math.sqrt

internal class BellShakeDetector {
    private var armed = true
    private var lastTime: Long? = null
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    fun strikeStrength(x: Float, y: Float, z: Float, timeNanos: Long): Float? {
        val magnitude = sqrt(x * x + y * y + z * z)
        if (!magnitude.isFinite()) return null
        if (magnitude < REARM_ACCELERATION) armed = true
        if (magnitude < STRIKE_ACCELERATION) return null
        val last = lastTime
        if (last != null && timeNanos - last < MIN_STRIKE_INTERVAL_NANOS) return null
        val reversed = x * lastX + y * lastY + z * lastZ < 0
        if (!armed && !reversed) return null

        armed = false
        lastTime = timeNanos
        lastX = x
        lastY = y
        lastZ = z
        return (0.55f + (magnitude - STRIKE_ACCELERATION) / 20f).coerceAtMost(1f)
    }

    private companion object {
        const val REARM_ACCELERATION = 3f
        const val STRIKE_ACCELERATION = 8f
        const val MIN_STRIKE_INTERVAL_NANOS = 90_000_000L
    }
}
