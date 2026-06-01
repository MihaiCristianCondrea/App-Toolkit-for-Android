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

package com.wstxda.toolkit.ui.icon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import androidx.core.graphics.createBitmap
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.breathing.BreathingPhase

class BreathingIconProvider(private val context: Context) {
    private val size = 100
    private val center = size / 2f
    private val maxRadius = size / 2f - 4f

    private val iconBitmap = createBitmap(size, size)
    private val canvas = Canvas(iconBitmap)

    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun getIcon(phase: BreathingPhase, progress: Float): Icon {
        if (phase == BreathingPhase.IDLE) {
            return Icon.createWithResource(context, R.drawable.ic_breathing)
        }

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        val currentRadius: Float
        val currentAlpha: Int

        if (phase == BreathingPhase.PREPARING) {
            currentRadius = maxRadius
            currentAlpha = 255
        } else {
            currentRadius = (maxRadius * 0.2f) + (maxRadius * 0.8f * progress)
            val minAlpha = 75
            currentAlpha = (minAlpha + ((255 - minAlpha) * progress)).toInt()
        }

        paint.alpha = currentAlpha
        canvas.drawCircle(center, center, currentRadius, paint)

        return Icon.createWithBitmap(iconBitmap)
    }
}